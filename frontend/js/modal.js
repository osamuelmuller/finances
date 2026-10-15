/**
 * Purchase Modal Controller
 * Gerenciador unificado de criação e edição de compras/transações integrado com a API
 */

import { PurchasesAPI, CategoriesAPI, PaymentMethodsAPI } from './api.js';
import { showToast } from './utils.js';

let modalElement = null;
let currentEditId = null;
let onSuccessCallback = null;

/**
 * Cria e insere o HTML do modal dinamicamente no DOM caso ainda não exista
 */
function ensureModalMounted() {
    if (document.getElementById('purchase-modal-backdrop')) {
        modalElement = document.getElementById('purchase-modal-backdrop');
        return;
    }

    const modalHtml = `
        <div id="purchase-modal-backdrop" class="modal-backdrop">
            <div class="modal-content">
                <header class="modal-header">
                    <h2 id="modal-title">Nova Transação</h2>
                    <button type="button" class="modal-close-btn" id="modal-close-x">&times;</button>
                </header>

                <form id="purchase-form" class="modal-form">
                    <div class="form-group">
                        <label for="purchase-description">Descrição *</label>
                        <input 
                            type="text" 
                            id="purchase-description" 
                            name="description" 
                            placeholder="Ex: Supermercado, Abastecimento, Almoço..." 
                            maxlength="50" 
                            required
                        >
                    </div>

                    <div class="form-group">
                        <label for="purchase-value">Valor (R$) *</label>
                        <input 
                            type="number" 
                            id="purchase-value" 
                            name="value" 
                            placeholder="0,00" 
                            step="0.01" 
                            min="0.01" 
                            required
                        >
                    </div>

                    <div class="form-group">
                        <label for="purchase-date">Data da Compra</label>
                        <input 
                            type="date" 
                            id="purchase-date" 
                            name="purchaseDate"
                        >
                    </div>

                    <div class="form-group">
                        <label for="purchase-category">Categoria *</label>
                        <select id="purchase-category" name="categoryId" required>
                            <option value="" disabled selected>Carregando categorias...</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="purchase-payment-method">Forma de Pagamento *</label>
                        <select id="purchase-payment-method" name="paymentMethodId" required>
                            <option value="" disabled selected>Carregando formas...</option>
                        </select>
                    </div>

                    <footer class="modal-footer">
                        <button type="button" class="btn tertiary--btn" id="modal-btn-cancel">Cancelar</button>
                        <button type="submit" class="btn primary--btn" id="modal-btn-submit">Salvar Transação</button>
                    </footer>
                </form>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', modalHtml);
    modalElement = document.getElementById('purchase-modal-backdrop');

    // Eventos de fechar
    document.getElementById('modal-close-x').addEventListener('click', closeModal);
    document.getElementById('modal-btn-cancel').addEventListener('click', closeModal);
    modalElement.addEventListener('click', (e) => {
        if (e.target === modalElement) closeModal();
    });

    // Evento de submit
    document.getElementById('purchase-form').addEventListener('submit', handleFormSubmit);
}

/**
 * Carrega as opções de categorias e métodos de pagamento na caixa de seleção
 * @param {number|null} selectedCategory
 * @param {number|null} selectedPaymentMethod
 */
async function loadFormSelects(selectedCategory = null, selectedPaymentMethod = null) {
    const categorySelect = document.getElementById('purchase-category');
    const paymentSelect = document.getElementById('purchase-payment-method');

    try {
        const [categories, paymentMethods] = await Promise.all([
            CategoriesAPI.getAll(),
            PaymentMethodsAPI.getAll()
        ]);

        // Popular Categorias
        categorySelect.innerHTML = '<option value="" disabled selected>Selecione uma categoria</option>';
        if (categories && categories.length > 0) {
            categories.forEach(cat => {
                const opt = document.createElement('option');
                opt.value = cat.id;
                opt.textContent = `${cat.name} (Disponível: R$ ${Number(cat.remainingBudget).toFixed(2)})`;
                if (selectedCategory && Number(selectedCategory) === Number(cat.id)) {
                    opt.selected = true;
                }
                categorySelect.appendChild(opt);
            });
        } else {
            categorySelect.innerHTML = '<option value="" disabled>Nenhuma categoria encontrada</option>';
        }

        // Popular Formas de Pagamento
        paymentSelect.innerHTML = '<option value="" disabled selected>Selecione uma forma de pagamento</option>';
        if (paymentMethods && paymentMethods.length > 0) {
            paymentMethods.forEach(pm => {
                const opt = document.createElement('option');
                opt.value = pm.id;
                opt.textContent = pm.name;
                if (selectedPaymentMethod && Number(selectedPaymentMethod) === Number(pm.id)) {
                    opt.selected = true;
                }
                paymentSelect.appendChild(opt);
            });
        } else {
            paymentSelect.innerHTML = '<option value="" disabled>Nenhuma forma cadastrada</option>';
        }

    } catch (err) {
        showToast('Erro ao carregar categorias ou formas de pagamento da API', 'error');
        console.error(err);
    }
}

/**
 * Abre o modal para cadastro de nova compra
 * @param {Function} [onSuccess] - Callback executado após salvar com sucesso
 */
export async function openCreatePurchaseModal(onSuccess) {
    ensureModalMounted();
    currentEditId = null;
    onSuccessCallback = onSuccess;

    document.getElementById('modal-title').textContent = 'Nova Transação';
    document.getElementById('modal-btn-submit').textContent = 'Salvar Transação';
    document.getElementById('purchase-form').reset();

    // Data de hoje como padrão (YYYY-MM-DD)
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('purchase-date').value = today;

    await loadFormSelects();

    modalElement.classList.add('active');
    document.getElementById('purchase-description').focus();
}

/**
 * Abre o modal para edição de uma compra existente
 * @param {number} purchaseId
 * @param {Function} [onSuccess] - Callback executado após salvar com sucesso
 */
export async function openEditPurchaseModal(purchaseId, onSuccess) {
    ensureModalMounted();
    currentEditId = purchaseId;
    onSuccessCallback = onSuccess;

    document.getElementById('modal-title').textContent = 'Editar Transação';
    document.getElementById('modal-btn-submit').textContent = 'Atualizar Transação';

    try {
        const purchase = await PurchasesAPI.getById(purchaseId);
        document.getElementById('purchase-description').value = purchase.description || '';
        document.getElementById('purchase-value').value = purchase.value || '';
        document.getElementById('purchase-date').value = purchase.date || '';

        // Obter todas as categorias e formas para encontrar o id correspondente pelo nome
        const [categories, paymentMethods] = await Promise.all([
            CategoriesAPI.getAll(),
            PaymentMethodsAPI.getAll()
        ]);

        const matchedCategory = categories.find(c => c.name === purchase.categoryName);
        const matchedPayment = paymentMethods.find(p => p.name === purchase.paymentMethodName);

        await loadFormSelects(
            matchedCategory ? matchedCategory.id : null, 
            matchedPayment ? matchedPayment.id : null
        );

        modalElement.classList.add('active');
    } catch (err) {
        showToast('Erro ao buscar dados da transação para edição', 'error');
        console.error(err);
    }
}

/**
 * Fecha o modal
 */
export function closeModal() {
    if (modalElement) {
        modalElement.classList.remove('active');
    }
}

/**
 * Manipulador de submissão do formulário do modal
 */
async function handleFormSubmit(e) {
    e.preventDefault();

    const description = document.getElementById('purchase-description').value.trim();
    const value = parseFloat(document.getElementById('purchase-value').value);
    const purchaseDate = document.getElementById('purchase-date').value || null;
    const categoryId = parseInt(document.getElementById('purchase-category').value, 10);
    const paymentMethodId = parseInt(document.getElementById('purchase-payment-method').value, 10);

    if (!description) {
        showToast('A descrição é obrigatória.', 'error');
        return;
    }

    if (isNaN(value) || value <= 0) {
        showToast('O valor deve ser maior que zero.', 'error');
        return;
    }

    if (!categoryId) {
        showToast('Selecione uma categoria válida.', 'error');
        return;
    }

    if (!paymentMethodId) {
        showToast('Selecione uma forma de pagamento.', 'error');
        return;
    }

    const payload = {
        description,
        value,
        purchaseDate,
        categoryId,
        paymentMethodId
    };

    const submitBtn = document.getElementById('modal-btn-submit');
    submitBtn.disabled = true;
    submitBtn.textContent = 'Gravando...';

    try {
        if (currentEditId) {
            await PurchasesAPI.update(currentEditId, payload);
            showToast('Transação atualizada com sucesso!', 'success');
        } else {
            await PurchasesAPI.create(payload);
            showToast('Transação cadastrada com sucesso!', 'success');
        }

        closeModal();
        if (typeof onSuccessCallback === 'function') {
            onSuccessCallback();
        }
    } catch (error) {
        showToast(`Erro ao salvar transação: ${error.message}`, 'error');
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = currentEditId ? 'Atualizar Transação' : 'Salvar Transação';
    }
}
