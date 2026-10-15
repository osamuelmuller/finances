/**
 * API Client - Finances Application
 * Conexão com a API Spring Boot (Java)
 * Base URL: http://localhost:8080/api
 */

const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Função utilitária para requisições HTTP padronizadas com tratamento de erros
 * @param {string} endpoint - Caminho relativo do endpoint (ex: '/purchases')
 * @param {RequestInit} [options={}] - Opções do fetch (method, headers, body, etc)
 * @returns {Promise<any>}
 */
async function fetchApi(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    const defaultHeaders = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    };

    const config = {
        ...options,
        headers: {
            ...defaultHeaders,
            ...options.headers
        }
    };

    try {
        const response = await fetch(url, config);

        // Se a resposta for 204 No Content ou DELETE sem retorno de corpo
        if (response.status === 204 || response.headers.get('content-length') === '0') {
            return null;
        }

        const contentType = response.headers.get('content-type');
        const isJson = contentType && contentType.includes('application/json');
        const data = isJson ? await response.json() : await response.text();

        if (!response.ok) {
            let errorMessage = `Erro HTTP: ${response.status} ${response.statusText}`;
            if (data) {
                if (typeof data === 'string') {
                    errorMessage = data;
                } else if (data.message) {
                    errorMessage = data.message;
                } else if (data.error) {
                    errorMessage = data.error;
                }
            }
            throw new Error(errorMessage);
        }

        return data;
    } catch (error) {
        console.error(`[API Error] Falha na chamada a ${url}:`, error);
        throw error;
    }
}

/* ==========================================================================
   ENDPOINTS: PURCHASES (Compras / Transações)
   Controller: PurchaseController.java (@RequestMapping("api/purchases"))
   ========================================================================== */

export const PurchasesAPI = {
    /**
     * Retorna todas as compras com suporte a filtros
     * GET /api/purchases?categoryId=&paymentMethodId=&startDate=&endDate=
     * @param {Object} filters
     * @param {number|string} [filters.categoryId]
     * @param {number|string} [filters.paymentMethodId]
     * @param {string} [filters.startDate] - Formato YYYY-MM-DD
     * @param {string} [filters.endDate] - Formato YYYY-MM-DD
     * @returns {Promise<Array<{id: number, description: string, value: number, date: string, categoryName: string, paymentMethodName: string}>>}
     */
    async getAll(filters = {}) {
        const params = new URLSearchParams();
        if (filters.categoryId) params.append('categoryId', filters.categoryId);
        if (filters.paymentMethodId) params.append('paymentMethodId', filters.paymentMethodId);
        if (filters.startDate) params.append('startDate', filters.startDate);
        if (filters.endDate) params.append('endDate', filters.endDate);

        const queryString = params.toString() ? `?${params.toString()}` : '';
        return await fetchApi(`/purchases${queryString}`, { method: 'GET' });
    },

    /**
     * Retorna uma compra pelo ID
     * GET /api/purchases/{id}
     * @param {number|string} id
     */
    async getById(id) {
        return await fetchApi(`/purchases/${id}`, { method: 'GET' });
    },

    /**
     * Cria uma nova compra
     * POST /api/purchases
     * Body: CreatePurchaseRequest { description, value, purchaseDate, categoryId, paymentMethodId }
     * @param {{description: string, value: number, purchaseDate: string, categoryId: number, paymentMethodId: number}} purchaseData
     */
    async create(purchaseData) {
        return await fetchApi('/purchases', {
            method: 'POST',
            body: JSON.stringify(purchaseData)
        });
    },

    /**
     * Atualiza uma compra existente
     * PUT /api/purchases/{id}
     * Body: UpdatePurchaseRequest { description, value, purchaseDate, categoryId, paymentMethodId }
     * @param {number|string} id
     * @param {{description: string, value: number, purchaseDate: string, categoryId: number, paymentMethodId: number}} purchaseData
     */
    async update(id, purchaseData) {
        return await fetchApi(`/purchases/${id}`, {
            method: 'PUT',
            body: JSON.stringify(purchaseData)
        });
    },

    /**
     * Deleta uma compra pelo ID
     * DELETE /api/purchases/{id}
     * @param {number|string} id
     */
    async delete(id) {
        return await fetchApi(`/purchases/${id}`, {
            method: 'DELETE'
        });
    }
};

/* ==========================================================================
   ENDPOINTS: CATEGORIES (Categorias)
   Controller: CategoryController.java (@RequestMapping("api/categories"))
   ========================================================================== */

export const CategoriesAPI = {
    /**
     * Retorna todas as categorias com orçamentos inicial e restante
     * GET /api/categories
     * @returns {Promise<Array<{id: number, name: string, initialBudget: number, remainingBudget: number}>>}
     */
    async getAll() {
        return await fetchApi('/categories', { method: 'GET' });
    },

    /**
     * Retorna uma categoria pelo ID
     * GET /api/categories/{id}
     * @param {number|string} id
     */
    async getById(id) {
        return await fetchApi(`/categories/${id}`, { method: 'GET' });
    },

    /**
     * Cria uma nova categoria
     * POST /api/categories
     * Body: CreateCategoryRequest { name, initialBudget }
     * @param {{name: string, initialBudget: number}} categoryData
     */
    async create(categoryData) {
        return await fetchApi('/categories', {
            method: 'POST',
            body: JSON.stringify(categoryData)
        });
    },

    /**
     * Atualiza uma categoria existente
     * PUT /api/categories/{id}
     * Body: UpdateCategoryRequest { name, initialBudget }
     * @param {number|string} id
     * @param {{name: string, initialBudget: number}} categoryData
     */
    async update(id, categoryData) {
        return await fetchApi(`/categories/${id}`, {
            method: 'PUT',
            body: JSON.stringify(categoryData)
        });
    },

    /**
     * Deleta uma categoria pelo ID
     * DELETE /api/categories/{id}
     * @param {number|string} id
     */
    async delete(id) {
        return await fetchApi(`/categories/${id}`, {
            method: 'DELETE'
        });
    }
};

/* ==========================================================================
   ENDPOINTS: PAYMENT METHODS (Métodos de Pagamento)
   Controller: PaymentMethodController.java (@RequestMapping("/api/payment-methods"))
   ========================================================================== */

export const PaymentMethodsAPI = {
    /**
     * Retorna todas as formas de pagamento
     * GET /api/payment-methods
     * @returns {Promise<Array<{id: number, name: string}>>}
     */
    async getAll() {
        return await fetchApi('/payment-methods', { method: 'GET' });
    },

    /**
     * Retorna uma forma de pagamento pelo ID
     * GET /api/payment-methods/{id}
     * @param {number|string} id
     */
    async getById(id) {
        return await fetchApi(`/payment-methods/${id}`, { method: 'GET' });
    },

    /**
     * Cria uma nova forma de pagamento
     * POST /api/payment-methods
     * Body: CreatePaymentMethodRequest { name }
     * @param {{name: string}} methodData
     */
    async create(methodData) {
        return await fetchApi('/payment-methods', {
            method: 'POST',
            body: JSON.stringify(methodData)
        });
    },

    /**
     * Atualiza uma forma de pagamento existente
     * PUT /api/payment-methods/{id}
     * Body: UpdatePaymentMethodRequest { name }
     * @param {number|string} id
     * @param {{name: string}} methodData
     */
    async update(id, methodData) {
        return await fetchApi(`/payment-methods/${id}`, {
            method: 'PUT',
            body: JSON.stringify(methodData)
        });
    },

    /**
     * Deleta uma forma de pagamento pelo ID
     * DELETE /api/payment-methods/{id}
     * @param {number|string} id
     */
    async delete(id) {
        return await fetchApi(`/payment-methods/${id}`, {
            method: 'DELETE'
        });
    }
};

/* ==========================================================================
   ENDPOINTS: TEST / STATUS
   Controller: TestController.java (@RequestMapping("/api/test"))
   ========================================================================== */

export const StatusAPI = {
    /**
     * Verifica se a API está online e respondendo
     * GET /api/test
     */
    async checkHealth() {
        return await fetchApi('/test', { method: 'GET' });
    }
};
