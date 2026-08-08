const API_URL = "http://localhost:8080";


// ==========================
// CATEGORIES
// ==========================

async function loadCategories() {

    const response = await fetch(
        `${API_URL}/api/categories`
    );

    const categories = await response.json();

    const container =
        document.getElementById("categories-container");

    container.innerHTML = "";

    categories.forEach(category => {

        const card = document.createElement("div");

        card.className = "category-card";

        card.innerHTML = `
            <h3>${category.name}</h3>

            <p>
                Initial Budget:
                R$ ${category.initialBudget}
            </p>

            <p>
                Remaining:
                R$ ${category.remainingBudget}
            </p>
        `;

        container.appendChild(card);
    });
}


// ==========================
// CATEGORY DROPDOWN
// ==========================

async function loadCategoryOptions() {

    const response = await fetch(
        `${API_URL}/api/categories`
    );

    const categories = await response.json();

    const select =
        document.getElementById("category");

    categories.forEach(category => {

        const option = document.createElement("option");

        option.value = category.id;

        option.textContent = category.name;

        select.appendChild(option);
    });
}


// ==========================
// PAYMENT METHODS
// ==========================

async function loadPaymentMethods() {

    const response = await fetch(
        `${API_URL}/api/payment-methods`
    );

    const paymentMethods = await response.json();

    const select =
        document.getElementById("payment-method");

    paymentMethods.forEach(method => {

        const option = document.createElement("option");

        option.value = method.id;

        option.textContent = method.name;

        select.appendChild(option);
    });
}


// ==========================
// PURCHASE FORM
// ==========================

const form =
    document.getElementById("purchase-form");

form.addEventListener("submit", async function(event) {

    event.preventDefault();

    const purchase = {

        description:
            document.getElementById("description").value,

        value:
            Number(document.getElementById("value").value),

        purchaseDate:
            document.getElementById("purchase-date").value || null,

        categoryId:
            Number(document.getElementById("category").value),

        paymentMethodId:
            Number(
                document.getElementById("payment-method").value
            )
    };

    const response = await fetch(
        `${API_URL}/api/purchases`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(purchase)
        }
    );

    if (!response.ok) {

        alert("Error creating purchase.");

        return;
    }

    const savedPurchase =
        await response.json();

    console.log(savedPurchase);

    alert("Purchase created!");

    form.reset();

    loadCategories();

    loadPurchases();
});


// ==========================
// PURCHASE HISTORY
// ==========================

async function loadPurchases() {

    const response = await fetch(
        `${API_URL}/api/purchases`
    );

    const purchases = await response.json();

    const container =
        document.getElementById("purchases-container");

    container.innerHTML = "";

    purchases.forEach(purchase => {

        const card =
            document.createElement("div");

        card.className = "purchase-card";

        card.innerHTML = `
            <h3>${purchase.description}</h3>

            <p>
                R$ ${purchase.value}
            </p>

            <p>
                ${purchase.date}
            </p>

            <p>
                Category:
                ${purchase.categoryName}
            </p>

            <p>
                Payment:
                ${purchase.paymentMethodName}
            </p>
        `;

        container.appendChild(card);
    });
}


// ==========================
// INITIAL LOAD
// ==========================

loadCategories();
loadCategoryOptions();
loadPaymentMethods();
loadPurchases();