/**
 * Index Page Controller (Dashboard)
 * Conecta o dashboard frontend à API do Spring Boot
 */

import { PurchasesAPI, CategoriesAPI } from './api.js';
import { 
    formatCurrency, 
    formatDate, 
    getCurrentYearMonth, 
    getMonthDateRange, 
    getCategoryColor, 
    getPaymentMethodTagClass,
    showToast 
} from './utils.js';
import { openCreatePurchaseModal } from './modal.js';

// Elementos do DOM
const elements = {
    calendar: document.getElementById('calendar'),
    btnNewPurchase: document.getElementById('btn-new-purchase'),
    currentBalance: document.getElementById('summary-current-balance'),
    totalBudget: document.getElementById('summary-total-budget'),
    totalExpenses: document.getElementById('summary-total-expenses'),
    totalSaved: document.getElementById('summary-total-saved'),
    categoriesList: document.getElementById('categories-list'),
    recentPurchasesList: document.getElementById('recent-purchases-list')
};

/**
 * Inicialização da página inicial
 */
document.addEventListener('DOMContentLoaded', () => {
    initCalendar();
    bindEvents();
    loadDashboardData();
});

/**
 * Inicializa o input de calendário com o mês atual
 */
function initCalendar() {
    if (elements.calendar) {
        elements.calendar.value = getCurrentYearMonth();
    }
}

/**
 * Registra ouvintes de eventos da página
 */
function bindEvents() {
    if (elements.calendar) {
        elements.calendar.addEventListener('change', () => {
            loadDashboardData();
        });
    }

    if (elements.btnNewPurchase) {
        elements.btnNewPurchase.addEventListener('click', () => {
            openCreatePurchaseModal(() => {
                loadDashboardData();
            });
        });
    }
}

/**
 * Carrega todos os dados necessários do backend e atualiza a interface
 */
async function loadDashboardData() {
    const selectedMonth = elements.calendar ? elements.calendar.value : getCurrentYearMonth();
    const { startDate, endDate } = getMonthDateRange(selectedMonth);

    try {
        // Exibir estados de carregamento iniciais se listas existirem
        if (elements.categoriesList) {
            elements.categoriesList.innerHTML = '<li class="state-loading">Carregando categorias...</li>';
        }
        if (elements.recentPurchasesList) {
            elements.recentPurchasesList.innerHTML = '<li class="state-loading">Carregando histórico...</li>';
        }

        // Buscar categorias e compras em paralelo da API Spring Boot
        const [categories, purchases] = await Promise.all([
            CategoriesAPI.getAll(),
            PurchasesAPI.getAll({ startDate, endDate })
        ]);

        // Garantir que apenas as compras do período selecionado sejam consideradas
        const periodPurchases = (purchases || []).filter(p => {
            if (!p.date || !startDate || !endDate) return true;
            return p.date >= startDate && p.date <= endDate;
        });

        renderSummary(categories, periodPurchases);
        renderCategories(categories, periodPurchases);
        renderRecentPurchases(periodPurchases);

    } catch (error) {
        console.error('Erro ao carregar dados do dashboard:', error);
        showToast('Não foi possível conectar à API Spring Boot. Verifique se o servidor está rodando na porta 8080.', 'error');
        
        if (elements.categoriesList) {
            elements.categoriesList.innerHTML = '<li class="state-empty">Erro ao carregar categorias do servidor.</li>';
        }
        if (elements.recentPurchasesList) {
            elements.recentPurchasesList.innerHTML = '<li class="state-empty">Erro ao carregar histórico do servidor.</li>';
        }
    }
}

/**
 * Calcula e renderiza os 4 cards da seção de resumo (.summary)
 * @param {Array} categories 
 * @param {Array} purchases 
 */
function renderSummary(categories = [], purchases = []) {
    // 1. Orçamento Total Planejado (Soma de initialBudget de todas as categorias)
    const totalPlannedBudget = categories.reduce((acc, cat) => acc + (Number(cat.initialBudget) || 0), 0);

    // 2. Total de Despesas no período selecionado (Soma de purchase.value)
    const totalExpensesValue = purchases.reduce((acc, p) => acc + (Number(p.value) || 0), 0);

    // 3. Saldo Atual Disponível (Soma de remainingBudget retornado pelo backend)
    const currentAvailableBalance = categories.reduce((acc, cat) => acc + (Number(cat.remainingBudget) || 0), 0);

    // 4. Total Economizado / Saldo Líquido (Orçamento Planejado - Despesas)
    const totalSavedValue = Math.max(0, totalPlannedBudget - totalExpensesValue);

    if (elements.currentBalance) {
        elements.currentBalance.textContent = formatCurrency(currentAvailableBalance);
    }
    if (elements.totalBudget) {
        elements.totalBudget.textContent = formatCurrency(totalPlannedBudget);
    }
    if (elements.totalExpenses) {
        elements.totalExpenses.textContent = formatCurrency(totalExpensesValue);
    }
    if (elements.totalSaved) {
        elements.totalSaved.textContent = formatCurrency(totalSavedValue);
    }
}

/**
 * Renderiza a lista de gastos por categoria (.category-summary .list)
 * considerando apenas as compras do período selecionado
 * @param {Array} categories 
 * @param {Array} purchases 
 */
function renderCategories(categories = [], purchases = []) {
    if (!elements.categoriesList) return;

    if (!categories || categories.length === 0) {
        elements.categoriesList.innerHTML = '<li class="state-empty">Nenhuma categoria cadastrada.</li>';
        return;
    }

    elements.categoriesList.innerHTML = '';

    categories.forEach((cat, index) => {
        const initial = Number(cat.initialBudget) || 0;

        // Filtra as compras do período selecionado pertencentes a esta categoria
        const categoryPurchases = (purchases || []).filter(p => {
            if (p.categoryId !== undefined && cat.id !== undefined) {
                return Number(p.categoryId) === Number(cat.id);
            }
            return (p.categoryName || '').trim().toLowerCase() === (cat.name || '').trim().toLowerCase();
        });

        // Gasto total desta categoria no período selecionado
        const spent = categoryPurchases.reduce((sum, p) => sum + (Number(p.value) || 0), 0);
        const remaining = Math.max(0, initial - spent);
        
        let percentage = 0;
        if (initial > 0) {
            percentage = Math.min(100, Math.round((spent / initial) * 100));
        }

        const color = getCategoryColor(cat.name, index);

        const li = document.createElement('li');
        li.className = 'list-item';
        li.innerHTML = `
            <div class="category--icon" style="background-color: ${color};"></div>
            <div class="list-infos">
                <div class="category-header">
                    <p>${escapeHtml(cat.name)}</p>
                    <p>${formatCurrency(spent)}</p>
                </div>
                <p>Disponível: ${formatCurrency(remaining)} / ${formatCurrency(initial)}</p>
            </div>
            <div class="spend-progress">
                <p>${percentage}%</p>
                <div class="progress-bar">
                    <div class="progress-width" style="width: ${percentage}%; background-color: ${color};"></div>
                </div>
            </div>
        `;
        elements.categoriesList.appendChild(li);
    });
}

/**
 * Renderiza o histórico recente de transações (.transaction-history .list)
 * @param {Array} purchases 
 */
function renderRecentPurchases(purchases = []) {
    if (!elements.recentPurchasesList) return;

    if (!purchases || purchases.length === 0) {
        elements.recentPurchasesList.innerHTML = '<li class="state-empty">Nenhuma transação encontrada para este mês.</li>';
        return;
    }

    // Ordenar pelas transações mais recentes (data decrescente ou id decrescente)
    const sorted = [...purchases].sort((a, b) => {
        if (a.date && b.date) {
            return new Date(b.date) - new Date(a.date);
        }
        return b.id - a.id;
    });

    // Limitar as 6 mais recentes no dashboard inicial
    const recent = sorted.slice(0, 6);

    elements.recentPurchasesList.innerHTML = '';

    recent.forEach((item, index) => {
        const tagClass = getPaymentMethodTagClass(item.paymentMethodName);
        const categoryColor = getCategoryColor(item.categoryName, index);

        const li = document.createElement('li');
        li.className = 'list-item';
        li.innerHTML = `
            <div class="category--icon" style="background-color: ${categoryColor};"></div>
            <div class="list-infos">
                <div class="category-header">
                    <p>${escapeHtml(item.description)}</p>
                    <p class="danger">- ${formatCurrency(item.value)}</p>
                </div>
                <div class="category-description">
                    <p>${escapeHtml(item.categoryName || 'Geral')}</p>
                    <p>${formatDate(item.date)}</p>
                </div>
            </div>
            <div class="tag-column">
                <span class="tag ${tagClass}">${escapeHtml(item.paymentMethodName || 'Outro')}</span>
            </div>
        `;
        elements.recentPurchasesList.appendChild(li);
    });
}

/**
 * Escapa strings contra XSS ao inserir no innerHTML
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
