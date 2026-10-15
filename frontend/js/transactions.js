/**
 * Transactions Page Controller
 * Conecta a página de histórico de transações com todos os filtros, tabela e ações da API
 */

import { PurchasesAPI, CategoriesAPI, PaymentMethodsAPI } from './api.js';
import { 
    formatCurrency, 
    formatDate, 
    getCurrentYearMonth, 
    getMonthDateRange, 
    getCategoryColor, 
    showToast 
} from './utils.js';
import { openCreatePurchaseModal, openEditPurchaseModal } from './modal.js';

// Elementos do DOM
const elements = {
    btnNewTransaction: document.getElementById('btn-new-transaction'),
    searchInput: document.getElementById('search'),
    calendarInput: document.getElementById('calendar'),
    categorySelect: document.getElementById('category-selection'),
    paymentMethodSelect: document.getElementById('payment-method-selection'),
    btnClearFilters: document.getElementById('btn-clear-filters'),
    
    totalPeriodIncome: document.getElementById('total-period-income'),
    totalPeriodExpense: document.getElementById('total-period-expense'),
    totalPeriodBalance: document.getElementById('total-period-balance'),

    tableBody: document.getElementById('transactions-table-body'),
    tableElement: document.querySelector('.table')
};

// Cache local de compras para pesquisa instantânea no campo de busca
let cachedPurchases = [];
let allCategories = [];

/**
 * Inicialização da página de transações
 */
document.addEventListener('DOMContentLoaded', async () => {
    initCalendar();
    bindEvents();
    await loadFilterOptions();
    await loadTransactions();
});

/**
 * Inicializa o input de calendário com o mês atual
 */
function initCalendar() {
    if (elements.calendarInput) {
        elements.calendarInput.value = getCurrentYearMonth();
    }
}

/**
 * Registra os ouvintes de eventos da página
 */
function bindEvents() {
    // Botão Nova Transação
    if (elements.btnNewTransaction) {
        elements.btnNewTransaction.addEventListener('click', () => {
            openCreatePurchaseModal(() => {
                loadTransactions();
            });
        });
    }

    // Filtros de mudança
    if (elements.calendarInput) {
        elements.calendarInput.addEventListener('change', () => loadTransactions());
    }

    if (elements.categorySelect) {
        elements.categorySelect.addEventListener('change', () => loadTransactions());
    }

    if (elements.paymentMethodSelect) {
        elements.paymentMethodSelect.addEventListener('change', () => loadTransactions());
    }

    // Busca textual com debounce
    if (elements.searchInput) {
        let debounceTimer;
        elements.searchInput.addEventListener('input', () => {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                applyLocalSearchFilter();
            }, 250);
        });
    }

    // Limpar filtros
    if (elements.btnClearFilters) {
        elements.btnClearFilters.addEventListener('click', (e) => {
            e.preventDefault();
            resetFilters();
        });
    }
}

/**
 * Reseta todos os campos de filtro para o estado inicial
 */
function resetFilters() {
    if (elements.searchInput) elements.searchInput.value = '';
    if (elements.calendarInput) elements.calendarInput.value = getCurrentYearMonth();
    if (elements.categorySelect) elements.categorySelect.value = '';
    if (elements.paymentMethodSelect) elements.paymentMethodSelect.value = '';
    loadTransactions();
}

/**
 * Popula os seletores de categoria e forma de pagamento com dados da API
 */
async function loadFilterOptions() {
    try {
        const [categories, paymentMethods] = await Promise.all([
            CategoriesAPI.getAll(),
            PaymentMethodsAPI.getAll()
        ]);

        allCategories = categories || [];

        // Popular Select de Categorias
        if (elements.categorySelect) {
            elements.categorySelect.innerHTML = '<option value="" selected>Todas as categorias</option>';
            allCategories.forEach(cat => {
                const opt = document.createElement('option');
                opt.value = cat.id;
                opt.textContent = cat.name;
                elements.categorySelect.appendChild(opt);
            });
        }

        // Popular Select de Formas de Pagamento
        if (elements.paymentMethodSelect) {
            elements.paymentMethodSelect.innerHTML = '<option value="" selected>Todas as formas de pagamento</option>';
            if (paymentMethods && paymentMethods.length > 0) {
                paymentMethods.forEach(pm => {
                    const opt = document.createElement('option');
                    opt.value = pm.id;
                    opt.textContent = pm.name;
                    elements.paymentMethodSelect.appendChild(opt);
                });
            }
        }
    } catch (err) {
        console.error('Erro ao carregar opções de filtro:', err);
        showToast('Não foi possível carregar os filtros da API.', 'error');
    }
}

/**
 * Carrega a lista de transações da API de acordo com os filtros de data, categoria e pagamento
 */
async function loadTransactions() {
    const selectedMonth = elements.calendarInput ? elements.calendarInput.value : '';
    const { startDate, endDate } = getMonthDateRange(selectedMonth);

    const categoryId = elements.categorySelect && elements.categorySelect.value 
        ? elements.categorySelect.value 
        : undefined;

    const paymentMethodId = elements.paymentMethodSelect && elements.paymentMethodSelect.value 
        ? elements.paymentMethodSelect.value 
        : undefined;

    setTableLoading(true);

    try {
        const purchases = await PurchasesAPI.getAll({
            categoryId,
            paymentMethodId,
            startDate,
            endDate
        });

        cachedPurchases = purchases || [];
        applyLocalSearchFilter();

    } catch (error) {
        console.error('Erro ao buscar transações:', error);
        showToast('Erro ao carregar transações da API Spring Boot.', 'error');
        setTableLoading(false);
        if (elements.tableBody) {
            elements.tableBody.innerHTML = `
                <tr>
                    <td colspan="7" class="state-empty">
                        Erro ao conectar com o backend. Verifique se o servidor está ativo.
                    </td>
                </tr>
            `;
        }
    }
}

/**
 * Aplica a busca textual sobre as transações já filtradas pelo backend
 */
function applyLocalSearchFilter() {
    const query = elements.searchInput ? elements.searchInput.value.trim().toLowerCase() : '';
    
    let filtered = cachedPurchases;
    if (query) {
        filtered = cachedPurchases.filter(p => {
            const desc = (p.description || '').toLowerCase();
            const cat = (p.categoryName || '').toLowerCase();
            const pm = (p.paymentMethodName || '').toLowerCase();
            return desc.includes(query) || cat.includes(query) || pm.includes(query);
        });
    }

    renderBalanceSummary(filtered);
    renderTable(filtered);
    setTableLoading(false);
}

/**
 * Atualiza os cards de balanço do período (entradas, saídas e saldo)
 * @param {Array} purchases 
 */
function renderBalanceSummary(purchases = []) {
    // Total de despesas (saídas) do conjunto filtrado
    const totalExpense = purchases.reduce((sum, p) => sum + (Number(p.value) || 0), 0);

    // Orçamento das categorias correspondentes como total de entradas planejadas
    const selectedCatId = elements.categorySelect ? elements.categorySelect.value : '';
    let totalIncome = 0;

    if (selectedCatId) {
        const cat = allCategories.find(c => String(c.id) === String(selectedCatId));
        totalIncome = cat ? (Number(cat.initialBudget) || 0) : 0;
    } else {
        totalIncome = allCategories.reduce((sum, c) => sum + (Number(c.initialBudget) || 0), 0);
    }

    const periodBalance = totalIncome - totalExpense;

    if (elements.totalPeriodIncome) {
        elements.totalPeriodIncome.textContent = formatCurrency(totalIncome);
    }
    if (elements.totalPeriodExpense) {
        elements.totalPeriodExpense.textContent = formatCurrency(totalExpense);
    }
    if (elements.totalPeriodBalance) {
        elements.totalPeriodBalance.textContent = formatCurrency(periodBalance);
    }
}

/**
 * Renderiza as linhas de transações na tabela
 * @param {Array} purchases 
 */
function renderTable(purchases = []) {
    if (!elements.tableBody) return;

    if (!purchases || purchases.length === 0) {
        elements.tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="state-empty">
                    Nenhuma transação encontrada com os filtros selecionados.
                </td>
            </tr>
        `;
        return;
    }

    // Ordenar por data decrescente
    const sorted = [...purchases].sort((a, b) => {
        if (a.date && b.date) {
            return new Date(b.date) - new Date(a.date);
        }
        return b.id - a.id;
    });

    elements.tableBody.innerHTML = '';

    sorted.forEach((item, index) => {
        const categoryColor = getCategoryColor(item.categoryName, index);

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>
                <div class="table--cell">
                    <div class="category--icon" style="background-color: var(--color-gray);"></div>
                    <div class="date-info">
                        <p style="color: var(--color-text-primary);">${formatDate(item.date)}</p>
                    </div>
                </div>
            </td>
            <td>
                <div class="table--cell">
                    <p style="color: var(--color-text-primary); font-weight: 500;">${escapeHtml(item.description)}</p>
                </div>
            </td>
            <td>
                <div class="table--cell">
                    <div class="category--icon" style="background-color: ${categoryColor};"></div>
                    <p style="color: var(--color-text-primary);">${escapeHtml(item.categoryName || 'Geral')}</p>
                </div>
            </td>
            <td>
                <div class="table--cell">
                    <p style="color: var(--color-text-primary);">${escapeHtml(item.paymentMethodName || 'Outro')}</p>
                </div>
            </td>
            <td>
                <div class="table--cell">
                    <span class="tag" style="background-color: var(--color-red);">Saída</span>
                </div>
            </td>
            <td>
                <div class="table--cell">
                    <p class="danger" style="flex-shrink: 0; font-weight: 600;">- ${formatCurrency(item.value)}</p>
                </div>
            </td>
            <td>
                <div class="table--cell action-btn-group">
                    <button class="btn-icon btn-edit" title="Editar transação" data-id="${item.id}">
                        ✏️
                    </button>
                    <button class="btn-icon btn-delete" title="Excluir transação" data-id="${item.id}">
                        🗑️
                    </button>
                </div>
            </td>
        `;

        // Eventos dos botões de ação
        const editBtn = tr.querySelector('.btn-edit');
        const deleteBtn = tr.querySelector('.btn-delete');

        editBtn.addEventListener('click', () => {
            openEditPurchaseModal(item.id, () => {
                loadTransactions();
            });
        });

        deleteBtn.addEventListener('click', async () => {
            if (confirm(`Deseja realmente excluir a transação "${item.description}"?`)) {
                try {
                    await PurchasesAPI.delete(item.id);
                    showToast('Transação excluída com sucesso!', 'success');
                    await loadTransactions();
                } catch (error) {
                    showToast(`Erro ao excluir transação: ${error.message}`, 'error');
                }
            }
        });

        elements.tableBody.appendChild(tr);
    });
}

/**
 * Controla o estado de exibição de carregamento da tabela
 * @param {boolean} isLoading 
 */
function setTableLoading(isLoading) {
    if (!elements.tableBody) return;
    if (isLoading) {
        elements.tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="state-loading">
                    Carregando transações da API...
                </td>
            </tr>
        `;
    }
}

/**
 * Escapa strings contra injeção de HTML
 */
function escapeHtml(text) {
    if (!text) return '';
    return String(text)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}
