/**
 * Utilities - Finances Application
 * Funções auxiliares de formatação, cores, cálculo de períodos e notificações
 */

/**
 * Formata um valor numérico para o padrão de moeda Real Brasileiro (BRL)
 * @param {number|string} value
 * @returns {string} Ex: "R$ 1.250,00"
 */
export function formatCurrency(value) {
    const num = Number(value) || 0;
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(num);
}

/**
 * Formata uma data no formato ISO "YYYY-MM-DD" para "DD/MM/YYYY"
 * @param {string} dateString
 * @returns {string} Ex: "15/09/2026"
 */
export function formatDate(dateString) {
    if (!dateString) return '-';
    const [year, month, day] = dateString.split('-');
    if (!year || !month || !day) return dateString;
    return `${day}/${month}/${year}`;
}

/**
 * Retorna o mês atual no formato aceito por inputs month ("YYYY-MM")
 * @returns {string} Ex: "2026-09"
 */
export function getCurrentYearMonth() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    return `${year}-${month}`;
}

/**
 * Retorna o primeiro e o último dia do mês para uso nos parâmetros startDate e endDate
 * @param {string} yearMonthString - Formato "YYYY-MM"
 * @returns {{startDate: string, endDate: string}}
 */
export function getMonthDateRange(yearMonthString) {
    if (!yearMonthString) return { startDate: '', endDate: '' };

    const [yearStr, monthStr] = yearMonthString.split('-');
    const year = parseInt(yearStr, 10);
    const month = parseInt(monthStr, 10);

    const firstDay = '01';
    const lastDayNum = new Date(year, month, 0).getDate();
    const lastDay = String(lastDayNum).padStart(2, '0');

    return {
        startDate: `${yearStr}-${String(month).padStart(2, '0')}-${firstDay}`,
        endDate: `${yearStr}-${String(month).padStart(2, '0')}-${lastDay}`
    };
}

/**
 * Mapeamento e fallback de cores por nome de categoria para a paleta do sistema
 */
const CATEGORY_COLORS = {
    'alimentação': 'var(--color-green)',
    'alimentacao': 'var(--color-green)',
    'moradia': 'var(--color-blue)',
    'transporte': 'var(--color-yellow)',
    'lazer': 'var(--color-purple)',
    'saúde': 'var(--color-red)',
    'saude': 'var(--color-red)',
    'educação': 'var(--color-cyan)',
    'educacao': 'var(--color-cyan)',
    'renda': 'var(--color-safe)'
};

const PALETTE_FALLBACKS = [
    'var(--color-green)',
    'var(--color-blue)',
    'var(--color-yellow)',
    'var(--color-purple)',
    'var(--color-red)',
    'var(--color-cyan)',
    'var(--color-gray)'
];

/**
 * Retorna uma cor temática para uma categoria
 * @param {string} categoryName
 * @param {number} [index=0]
 * @returns {string} Cor CSS
 */
export function getCategoryColor(categoryName = '', index = 0) {
    const normalized = categoryName.trim().toLowerCase();
    if (CATEGORY_COLORS[normalized]) {
        return CATEGORY_COLORS[normalized];
    }
    return PALETTE_FALLBACKS[index % PALETTE_FALLBACKS.length];
}

/**
 * Retorna a classe ou cor de tag para a forma de pagamento
 * @param {string} paymentMethodName
 * @returns {string} Nome da classe CSS da tag
 */
export function getPaymentMethodTagClass(paymentMethodName = '') {
    const normalized = paymentMethodName.trim().toLowerCase();
    if (normalized.includes('pix')) return 'tag--pix';
    if (normalized.includes('crédito') || normalized.includes('credito')) return 'tag--credito';
    if (normalized.includes('débito') || normalized.includes('debito')) return 'tag--debito';
    if (normalized.includes('depósito') || normalized.includes('deposito')) return 'tag--deposito';
    return '';
}

/**
 * Exibe uma notificação toast temporária na interface
 * @param {string} message
 * @param {'success'|'error'|'info'} [type='info']
 * @param {number} [duration=3500]
 */
export function showToast(message, type = 'info', duration = 3500) {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast-message toast--${type}`;
    toast.textContent = message;

    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('toast--fade-out');
        setTimeout(() => toast.remove(), 400);
    }, duration);
}
