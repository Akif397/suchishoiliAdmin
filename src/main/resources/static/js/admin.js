//variables in the sidebar
let sidebar_add_order = document.querySelector("#sidebar_add_order");
let sidebar_order_list = document.querySelector("#sidebar_order_list");
let sidebar_add_inventory = document.querySelector("#sidebar_add_inventory");
let sidebar_watch_inventory = document.querySelector("#sidebar_inventory_list");
let sidebar_expense = document.querySelector("#sidebar_expense");
let sidebar_add_category = document.querySelector("#sidebar_add_category");

//variables for the admin page content
let content_add_order = document.querySelector(".add_order");
let content_order_list = document.querySelector(".order_list");
let content_add_inventory = document.querySelector(".add_inventory");
let content_watch_inventory = document.querySelector(".watch_inventory");
let content_expense = document.querySelector(".expense");
let content_add_category = document.querySelector(".add_category");

//global variables
var productsFromDB = [];
var productForOrder = {
    id: 0,
    name: "",
    size: "",
    prize: 0,
    quantity: 0,
    discount: 0
};
/*var clientOrder = {
	clientName: "",
	clientPhoneNumber: "",
	clientLocation: "",
	clientAddress: "",
	clientNote: "",
	products: [],
	paymentStatus: "",
	paymentMethod: "",
	orderDiscount: 0,
	orderFrom: ""
}*/
var productsForOrder = [];


//variables for the form submissions
let add_inventory_submit = document.querySelector("#add_inventory_submit");

//variables for addInventory.html
let inventoryProductAddButton = document.querySelector("#inventoryProductSizeAddButton");
let inventoryProductSizeSaveButton = document.querySelector("#inventoryProductSizeSaveButton");
let inventoryProductSubcategory = document.querySelector("#inventoryProductSubcategory");
let multipleSizesCheckbox = document.querySelector("#multipleSizesCheckbox");

//variables for addorder.html
let add_order_discount = document.querySelector("#add_order_discount");
let add_order_order_discount_button = document.querySelector("#add_order_order_discount_button");

//eventlisteners of sidebar
sidebar_add_order.addEventListener("click", () => {
    display_admin_content(content_add_order.classList[0]);
});

sidebar_order_list.addEventListener("click", () => {
    display_admin_content(content_order_list.classList[0]);
});

sidebar_add_inventory.addEventListener("click", () => {
    display_admin_content(content_add_inventory.classList[0]);
});

sidebar_watch_inventory.addEventListener("click", () => {
    display_admin_content(content_watch_inventory.classList[0]);
});

sidebar_expense.addEventListener("click", () => {
    display_admin_content(content_expense.classList[0]);
});

sidebar_add_category.addEventListener("click", () => {
    display_admin_content(content_add_category.classList[0]);
});

//addInventory eventlisteners
multipleSizesCheckbox.addEventListener("change", () => {
    if (multipleSizesCheckbox.checked) {
        console.log("Checked");
    } else {
        console.log("Not-checked");
    }
});

inventoryProductSubcategory.addEventListener("change", () => {
    let subcategory = document.querySelector("#inventoryProductSubcategory").value;
    let url = "productSizesForInventory";
    let value = {subcategory: subcategory};
    $.ajax({
        type: "GET",
        url: url,
        data: value, // serializes the form's elements.
        dataType: 'json',
        success: function (returnData) {
            let select = document.querySelector("#inventoryProductSizeSelect");
            for (let i = select.options.length - 1; i > 0; i--) {
                select.remove(i);
            }
            if (returnData.length > 0) {
                for (let i = 0; i < returnData.length; i++) {
                    let option = document.createElement("option");
                    option.text = returnData[i].size;
                    option.value = returnData[i].size;
                    select.appendChild(option);
                }
                console.log(returnData);
            }
            inventoryProductAddButton.disabled = false;
        }
    });
});

inventoryProductSizeSaveButton.addEventListener("click", () => {
    let sizeSelect = document.querySelector("#inventoryProductSizeSelect");
    let size = document.querySelector("#productSize").value;
    let isSizeUnique = false;
    for (let i = 1; i < sizeSelect.options.length; i++) {
        if (sizeSelect.options[i].text.toLowerCase() === size.toLowerCase()) {
            isSizeUnique = true;
        }
    }
    if (!isSizeUnique && size !== "") {
        let inventoryProductSizeAddDiv = document.querySelector("#inventoryProductSizeAddDiv");
        let subcategory = document.querySelector("#inventoryProductSubcategory").value;
        let url = "submitProductSize";
        let value = {
            size: size,
            subcategory: subcategory
        };
        $.ajax({
            type: "POST",
            url: url,
            data: value, // serializes the form's elements.
            dataType: 'json',
            success: function (returnData) {
                inventoryProductSizeAddDiv.classList.toggle("show", false);
                inventoryProductAddButton.classList.toggle("hide", false);
                document.querySelector("#productSize").value = "";
                document.querySelector("#inventoryProductSizeSelect").disabled = false;
                var option = document.createElement("option");
                option.text = returnData.size;
                option.value = returnData.size;
                var select = document.querySelector("#inventoryProductSizeSelect");
                select.appendChild(option);
            }
        });
    } else {
        inventoryProductSizeAddDiv.classList.toggle("show", false);
        inventoryProductAddButton.classList.toggle("hide", false);
        document.querySelector("#productSize").value = "";
        document.querySelector("#inventoryProductSizeSelect").disabled = false;
    }
});

inventoryProductAddButton.addEventListener("click", () => {
    let inventoryProductSizeAddDiv = document.querySelector("#inventoryProductSizeAddDiv");
    inventoryProductSizeAddDiv.classList.toggle("show", true);
    inventoryProductAddButton.classList.toggle("hide", true);
    document.querySelector("#inventoryProductSizeSelect").disabled = true;
});

/*add_inventory_submit.addEventListener("click", () => {
	//alert("A");
	console.log("A");
	$.ajax({
		type: form.attr('method'), // method attribute of form
		url: form.attr('action'),  // action attribute of form
		dataType: 'json',
		// convert form data to json format
		data: JSON.stringify(formData),
	});
});*/

/*add_inventory_form.submit((e) => {
	alert("q");
});*/


//functions for the eventlisteners of sidebar
function display_admin_content(clicked_item) {
    if (clicked_item === "add_order") {
        display_content_add_order();
    } else if (clicked_item === "order_list") {
        display_content_order_list();
    } else if (clicked_item === "add_inventory") {
        display_content_add_inventory();
    } else if (clicked_item === "watch_inventory") {
        display_content_watch_inventory();
    } else if (clicked_item === "expense") {
        display_content_expense();
    } else if (clicked_item === "add_category") {
        display_content_add_category();
    }
}

function display_content_add_order() {
    content_add_order.classList.add('show');
    content_order_list.classList.remove("show");
    content_add_inventory.classList.remove('show');
    content_watch_inventory.classList.remove('show');
    content_expense.classList.remove('show');
    content_add_category.classList.remove('show');
}

function display_content_order_list() {
    content_add_order.classList.remove('show');
    content_order_list.classList.add("show");
    content_add_inventory.classList.remove('show');
    content_watch_inventory.classList.remove('show');
    content_expense.classList.remove('show');
    content_add_category.classList.remove('show');
}

function display_content_add_inventory() {
    content_add_order.classList.remove('show');
    content_order_list.classList.remove("show");
    content_add_inventory.classList.add('show');
    content_watch_inventory.classList.remove('show');
    content_expense.classList.remove('show');
    content_add_category.classList.remove('show');
}

function display_content_watch_inventory() {
    content_add_order.classList.remove('show');
    content_order_list.classList.remove("show");
    content_add_inventory.classList.remove('show');
    content_watch_inventory.classList.add('show');
    content_expense.classList.remove('show');
    content_add_category.classList.remove('show');
}

function display_content_expense() {
    content_add_order.classList.remove('show');
    content_order_list.classList.remove("show");
    content_add_inventory.classList.remove('show');
    content_watch_inventory.classList.remove('show');
    content_expense.classList.add('show');
}

function display_content_add_category() {
    content_add_order.classList.remove('show');
    content_order_list.classList.remove("show");
    content_add_inventory.classList.remove('show');
    content_watch_inventory.classList.remove('show');
    content_expense.classList.remove('show');
    content_add_category.classList.add('show');
}

function addInventorySubmitForm() {
    let category = document.querySelector("#inventoryProductCategory").value;
    let subCategory = document.querySelector("#inventoryProductSubcategory").value;
    let productName = document.querySelector("#productName").value;
    let productDescription = document.querySelector("#productDescription").value;
    let productSize = document.querySelector("#inventoryProductSizeSelect").value;
    let productPrize = document.querySelector("#productPrize").value;

    let url = "submitAddProduct";
    let value = {
        category: category,
        subCategory: subCategory,
        productName: productName,
        productDescription: productDescription,
        productSize: productSize,
        productPrize: productPrize,
        // productQuantity: productQuantity
    }


    if (category === "Select a Category") {
        alert("Select a Category.")
        document.querySelector("#inventoryProductCategory").focus();
        return;
    }
    if (subCategory === "Select a Sub-category") {
        alert("Select a Sub-category.")
        document.querySelector("#inventoryProductSubcategory").focus();
        return;
    }
    if (productName.length === 0) {
        alert("Product Name Missing.")
        document.querySelector("#productName").focus();
        return;
    }
    if (productDescription.length === 0) {
        alert("Product Description Missing.")
        document.querySelector("#productDescription").focus();
        return;
    }
    if (productPrize.length === 0) {
        alert("Product Prize Missing.")
        document.querySelector("#productPrize").focus();
        return;
    }
    if (productSize === "Size") {
        alert("Product Size Missing.")
        document.querySelector("#inventoryProductSizeSelect").focus();
        return;
    }

    $.ajax({
        type: "POST",
        url: url,
        data: value, // serializes the form's elements.
        dataType: 'json',
        success: function (returnData) {
            console.log(returnData);
            if (returnData === 1) {
                let addSuccess = $('#successDiv');
                addSuccess.toggleClass('show');
                if (!multipleSizesCheckbox.checked) {
                    document.querySelector("#inventoryProductCategory").options[0].selected = true;
                    for (let i = document.querySelector("#inventoryProductSubcategory").options.length - 1; i > 0 ; i--){
                        document.querySelector("#inventoryProductSubcategory").options[i] = null;
                    }
                    document.querySelector("#inventoryProductSubcategory").options[0].selected = true;
                    document.querySelector("#productName").value = "";
                    document.querySelector("#productName").placeholder = "Enter a name";
                    document.querySelector("#productDescription").value = "";
                    document.querySelector("#productDescription").placeholder = "Description";
                    document.querySelector("#productPrize").value = "";
                    document.querySelector("#productPrize").placeholder = "Prize";
                    for (let i = document.querySelector("#inventoryProductSizeSelect").options.length - 1; i > 0; i--){
                        document.querySelector("#inventoryProductSizeSelect").options[i] = null;
                    }
                    document.querySelector("#inventoryProductSizeSelect").options[0].selected = true;
                } else {
                    document.querySelector("#inventoryProductSizeSelect").options[0].selected = true;
                }

                // let productSizeSelect = document.querySelector("#inventoryProductSizeSelect");
                // for (let i = 1; i < productSizeSelect.options.length; i++) {
                //     productSizeSelect.options[i] = null;
                // }
            } else if (returnData === 0) {
                let addDuplicateEntry = $('#duplicateEntryDiv');
                addDuplicateEntry.toggleClass('show');
            }
        },
        error: function (jq, status, message) {
            alert('A jQuery error has occurred. Status: ' + status + ' - Message: ' + message);
        }
    });
}

function addOrderSubmitForm() {
    let clientName = document.querySelector("#add_order_customer_name").value;
    let clientPhoneNumber = document.querySelector("#add_order_customer_phone_no").value;
    let clientLocation = document.querySelector("#add_order_customer_location").value;
    let clientAddress = document.querySelector("#add_order_customer_address").value;
    let clientNote = document.querySelector("#add_order_note").value;
    let paymentStatus = document.querySelector("#add_order_payment_status").value;
    let paymentMethod = document.querySelector("#add_order_payment_method").value;
    let orderDiscount = (document.querySelector("#add_order_discount").value.trim() === "" ? 0 : parseInt(document.querySelector("#add_order_discount").value));
    let orderFrom = document.querySelector("#add_order_from").value;
    let products = [...productsForOrder];

    let url = "submitAddOrder";
    let value = {
        clientName: clientName,
        clientPhoneNumber: clientPhoneNumber,
        clientLocation: clientLocation,
        clientAddress: clientAddress,
        clientNote: clientNote,
        products: JSON.stringify(products),
        paymentStatus: paymentStatus,
        paymentMethod: paymentMethod,
        orderDiscount: orderDiscount,
        orderFrom: orderFrom
    };
    if (clientName.length === 0) {
        alert("Enter Customer Name.");
        document.querySelector("#add_order_customer_name").focus();
        return;
    }
    if (clientPhoneNumber.length === 0) {
        alert("Enter Customer Phone Number.");
        document.querySelector("#add_order_customer_phone_no").focus();
        return;
    }
    if (clientLocation === "Select Location") {
        alert("Enter Customer Location.");
        document.querySelector("#add_order_customer_location").focus();
        return;
    }
    if (clientAddress.length === 0) {
        alert("Enter Customer Address.");
        document.querySelector("#add_order_customer_address").focus();
        return;
    }
    if (products.length === 0) {
        alert("Add a product.");
        document.querySelector("#add_order_product_category").focus();
        return;
    }
    if (paymentStatus === "Select Payment Status") {
        alert("Select Payment Status.");
        document.querySelector("#add_order_payment_status").focus();
        return;
    }
    if (paymentMethod === "Select Payment Method") {
        alert("Select Payment Method.");
        document.querySelector("#add_order_payment_method").focus();
        return;
    }
    if (orderFrom === "Select Order From") {
        alert("Select Order From.");
        document.querySelector("#add_order_from").focus();
        return;
    }
    $.ajax({
        type: "POST",
        url: url,
        data: value, // serializes the form's elements.
        dataType: 'json',
        success: function (returnData) {
            if (returnData === 1) {
                document.querySelector("#add_order_customer_name").value = "";
                document.querySelector("#add_order_customer_name").placeholder = "Customer Name";
                document.querySelector("#add_order_customer_phone_no").value = "";
                document.querySelector("#add_order_customer_phone_no").placeholder = "Customer Phone No.";
                document.querySelector("#add_order_customer_location").options[0].selected = true;
                document.querySelector("#add_order_customer_address").value = "";
                document.querySelector("#add_order_customer_address").placeholder = "Address";
                document.querySelector("#add_order_note").value = "";
                document.querySelector("#add_order_note").placeholder = "Note";
                document.querySelector("#add_order_payment_status").options[0].selected = true;
                document.querySelector("#add_order_payment_method").options[0].selected = true;
                document.querySelector("#add_order_discount").value = "";
                document.querySelector("#add_order_discount").placeholder = "Discount";
                document.querySelector("#add_order_from").options[0].selected = true;
                document.querySelector("#add_order_order_discount_button").disabled = false;

                for (let i = 0; i < document.querySelector("#add_order_product_table").rows.length; i++) {
                    document.querySelector("#add_order_product_table").deleteRow(i);
                }

                let addSuccess = $('#successDiv');
                addSuccess.toggleClass('show');
            }
        }
    });
}

//functions for addCategory.html
function addCategorySubmitForm() {
    let category = document.querySelector("#categoryName").value;
    let categoryComboOptions = document.querySelector("#productCategorySelect").options;
    let subcategory = document.querySelector("#subCategryName").value;
    $("#add_category_form").submit(function (e) {
        e.preventDefault(); // avoid to execute the actual submit of the form.
        let form = $(this);
        let url = "submitAddCategory";
        let value = {
            category: category,
            subCategory: subcategory
        }
        $.ajax({
            type: "POST",
            url: url,
            data: value, // serializes the form's elements.
            dataType: 'json',
            success: function (returnData) {
                form[0].reset();
                let addSuccess = $('#successDiv');
                let is_new_category = false;
                for (let i = 0; i < categoryComboOptions.length; i++) {
                    if (i !== 0 && category !== categoryComboOptions[i].text) {
                        is_new_category = true;
                    } else if (i !== 0 && category === categoryComboOptions[i].text) {
                        is_new_category = false;
                        break;
                    }
                }
                if (is_new_category) {
                    let productCategorySelect = $('#productCategorySelect');
                    productCategorySelect.append('<option value="' + returnData.category + '">' + returnData.category + '</option>)');
                }
                addSuccess.toggleClass('show');
            }
        });
    });
}

function findSubcategoryForWatchInventory(categorySelectId, subcategorySelectID) {
    findSubcategoryFor(categorySelectId, subcategorySelectID);
    document.getElementById(categorySelectId).disabled = true;
}

function resetWatchInventoryFilter() {
    let category = document.querySelector("#productCategoryWatchInventorySelect");
    let subCategory = document.querySelector("#productSubcategoryWatchInventorySelect");
    category.disabled = false;
    subCategory.disabled = false;
}

function findSubcategory() {
    let url = "findSubCategory";
    let categorySelect = document.querySelector("#productCategorySelect");
    let category = categorySelect.options[categorySelect.selectedIndex].value;
    let value = {
        category: category
    }
    $.ajax({
        type: "GET",
        url: url,
        data: value,
        dataType: 'json',
        success: function (returnData) {
            let subcategoryList = returnData;
            let productSubCategoryTableBody = $("#productSubCategoryTableBody");
            let productSubCategoryTableHTML = populateproductSubcategoryTable(subcategoryList);
            productSubCategoryTableBody.html(productSubCategoryTableHTML);
        }
    });
}

function findSubcategoryAddOrder(categorySelectId, subcategorySelectID) {
    findSubcategoryFor(categorySelectId, subcategorySelectID);
    let nameSelector = document.querySelector("#add_order_product_name");
    let sizeSelector = document.querySelector("#add_order_product_size");
    for (i = nameSelector.options.length - 1; i >= 1; i--) {
        nameSelector.options[i] = null;
    }
    nameSelector.options[0].selected = true;
    for (i = sizeSelector.options.length - 1; i >= 1; i--) {
        sizeSelector.options[i] = null;
    }
    sizeSelector.options[0].selected = true;
}

function findSubcategoryFor(categorySelectId, subcategorySelectID) {
    let url = "findSubCategory";
    let category = document.querySelector("#" + categorySelectId + "").value;
    let value = {
        category: category
    }
    $.ajax({
        type: "GET",
        url: url,
        data: value,
        dataType: 'json',
        success: function (returnData) {
            let subcategoryList = returnData;
            let inventoryProductSubcategory = $('#' + subcategorySelectID + '');
            for (i = document.querySelector('#' + subcategorySelectID + '').options.length - 1; i >= 1; i--) {
                document.querySelector('#' + subcategorySelectID + '').options[i] = null;
            }
            for (let i = 0; i < subcategoryList.length; i++) {
                let subCategory = subcategoryList[i];
                inventoryProductSubcategory.append('<option value="' + subCategory + '">' + subCategory + '</option>)');
            }
            document.querySelector("#" + subcategorySelectID + "").options[0].selected = true;
        }
    });
}

function findProductSizeAddOrder() {
    let products = [...productsFromDB];
    let selectedProduct = document.querySelector("#add_order_product_name").value;
    for (let i = 0; i < products.length; i++) {
        if (products[i].name === selectedProduct) {
            let add_order_product_size = $('#add_order_product_size');
            for (j = document.querySelector('#add_order_product_size').options.length - 1; j >= 1; j--) {
                document.querySelector('#add_order_product_size').options[j] = null;
            }
            for (let j = 0; j < products[i].sizes.length; j++) {
                let size = products[i].sizes[j];
                add_order_product_size.append('<option value="' + size + '">' + size + '</option>');
            }
        }
    }
}

function populateProductTableAddOrder() {
    let add_order_product_table_body = document.querySelector("#add_order_product_table_body");
    let productTableBodyHTML = "";
    let count = 1;
    for (let i = 0; i < productsForOrder.length; i++) {
        let product = productsForOrder[i];
        let innerHTML = "<tr>" +
            "<td>" + count + "</td>" +
            "<td>" + product.name + "</td>" +
            "<td>" + product.size + "</td>" +
            "<td>" + product.quantity + "</td>" +
            "<td>" +
            "<button type=\"button\" class=\"btn btn-outline-danger btn-sm\" " +
            "onclick=\"deleteProductAddOrder(" + i + ")\">Remove</button>" +
            "</td></tr>";
        productTableBodyHTML += innerHTML;
        count++;
    }
    add_order_product_table_body.innerHTML = productTableBodyHTML;
}

function addProductToOrderList() {
    let add_order_product_category = document.querySelector("#add_order_product_category");
    let add_order_product_subcategory = document.querySelector("#add_order_product_subcategory");
    let add_order_product_name = document.querySelector("#add_order_product_name");
    let add_order_product_size = document.querySelector("#add_order_product_size");
    let add_order_product_quantity = document.querySelector("#add_order_product_quantity");
    let add_order_product_discount = document.querySelector("#add_order_product_discount");

    let product_name = add_order_product_name.value;
    let product_size = add_order_product_size.value;
    let product_quantity = parseInt(add_order_product_quantity.value);
    let product_discount = (add_order_product_discount.value === "" ? 0 : parseInt(add_order_product_discount.value));

    if (add_order_product_category.value !== "" && add_order_product_subcategory.value !== ""
        && product_name !== "" && product_size !== "" && product_quantity > 0) {
        let products = [...productsFromDB];
        let product = {...productForOrder};
        let add_order_total_prize = document.querySelector("#add_order_total_prize");

        for (let i = 0; i < products.length; i++) {
            if (products[i].name === product_name) {
                product.id = products[i].id;
                product.name = products[i].name;
                product.prize = products[i].prize;
                product.quantity = product_quantity;
                product.discount = product_discount;
                for (let j = 0; j < products[i].sizes.length; j++) {
                    if (products[i].sizes[j] === product_size) {
                        product.size = products[i].sizes[j];
                        break;
                    }
                }
                productsForOrder.push(product);
            }
        }
        populateProductTableAddOrder();
        let order_total_prize = parseInt(add_order_total_prize.innerHTML) + (product.prize * product.quantity) - product.discount;
        add_order_total_prize.innerHTML = order_total_prize;


        add_order_product_category.options[0].selected = true;

        for (j = add_order_product_subcategory.options.length - 1; j >= 1; j--) {
            add_order_product_subcategory.options[j] = null;
        }
        add_order_product_subcategory.options[0].selected = true;

        for (j = add_order_product_name.options.length - 1; j >= 1; j--) {
            add_order_product_name.options[j] = null;
        }
        add_order_product_name.options[0].selected = true;

        for (j = add_order_product_size.options.length - 1; j >= 1; j--) {
            add_order_product_size.options[j] = null;
        }
        add_order_product_size.options[0].selected = true;

        add_order_product_quantity.value = "";
        add_order_product_quantity.placeholder = "Quantity";

        add_order_product_discount.value = "";
        add_order_product_discount.placeholder = "Discount";
    } else {
        alert("Please Select a product to add");
        document.querySelector("#add_order_product_category").focus();
    }
}

function findProductAddOrder() {
    let url = "findProductBySubcategory";
    let subcategory = document.querySelector("#add_order_product_subcategory").value;
    let value = {
        subcategory: subcategory
    }
    $.ajax({
        type: "GET",
        url: url,
        data: value,
        dataType: 'json',
        success: function (returnData) {
            console.log(returnData);
            productsFromDB = returnData;
            let productList = returnData;
            let add_order_product_name = $('#add_order_product_name');

            for (i = document.querySelector('#add_order_product_name').options.length - 1; i >= 1; i--) {
                document.querySelector('#add_order_product_name').options[i] = null;
            }
            for (i = document.querySelector('#add_order_product_size').options.length - 1; i >= 1; i--) {
                document.querySelector('#add_order_product_size').options[i] = null;
            }
            for (let i = 0; i < productList.length; i++) {
                let name = productList[i].name;
                add_order_product_name.append('<option value="' + name + '">' + name + '</option>');
            }
            document.querySelector("#add_order_product_size").options[0].selected = true;
            document.querySelector("#add_order_product_name").options[0].selected = true;
        }
    });
}

function deleteSubcategory(subcategory) {
    let url = "deleteSubCategory";
    let value = {
        category: $("#productCategorySelect").val(),
        subCategory: subcategory
    }
    $.ajax({
        type: "DELETE",
        url: url,
        data: value,
        dataType: 'json',
        success: function (returnData) {
            console.log(returnData);
            let productSubCategoryTableBody = $("#productSubCategoryTableBody");
            let productSubCategoryTableHTML = populateproductSubcategoryTable(returnData.subCategories);
            productSubCategoryTableBody.html(productSubCategoryTableHTML);
            let addSuccess = $('#successDiv');
            addSuccess.toggleClass('show');
        }
    });
}

function addSuccessHide() {
    let addSuccess = $('#successDiv');
    addSuccess.toggleClass('show');
}

function addFailedHide() {
    let addFailed = $('#failDiv');
    addFailed.toggleClass('show');
}

function duplicateEntryHide() {
    let addDuplicateEntry = $('#duplicateEntryDiv');
    addDuplicateEntry.toggleClass('show');
}

function populateproductSubcategoryTable(subcategoryList) {
    let productSubCategoryTableHTML = "";
    for (let i = 0; i < subcategoryList.length; i++) {
        let subCategory = subcategoryList[i].subCategory;
        let innerHTML = "<tr>" +
            "<td>" + subCategory + "</td>" +
            "<td><button type=\"button\" " +
            "class=\"btn btn-danger\" " +
            "id=\"productSubcategory_" + subcategoryList[i].subCategory + "\" " +
            "onclick=\"deleteSubcategory(\'" + subCategory + "\')\">Remove</button></td></tr>";
        productSubCategoryTableHTML += innerHTML;
    }
    return productSubCategoryTableHTML;
}

function populateProductTable() {
    let subcategorySelect = document.querySelector("#productSubcategoryWatchInventorySelect");
    let subcategory = subcategorySelect.options[subcategorySelect.selectedIndex].value;
    subcategorySelect.disabled = true;
    let categorySelect = document.querySelector("#productCategoryWatchInventorySelect");
    categorySelect.disabled = true;
    let url = "findProductBySubcategory";
    let value = {
        subcategory: subcategory
    }
    $.ajax({
        type: "GET",
        url: url,
        data: value,
        dataType: 'json',
        success: function (returnData) {
            let productTableBody = $("#productTableBody");
            let productTableBodyHTML = "";
            for (let i = 0; i < returnData.length; i++) {
                let product = returnData[i];
                let innerHTML = "<tr>" +
                    "<td>" + product.name + "</td>" +
                    "<td class=\"text-warning font-weight-bold\">" + product.prize + "</td>" +
                    "<td><table class=\"table table-hover table-dark table-borderless\"";
                for (let j = 0; j < product.sizes.length; j++) {
                    innerHTML += "<tr><td>" + product.sizes[j] + "</td></tr>";
                }
                innerHTML += "</table></td>" +
                    "<td><table class=\"table table-hover table-dark table-borderless\">";
                // for (let j = 0; j < product.sizes.length; j++) {
                // 	innerHTML += "<tr><td>" + product.sizesAndQuantities[j].quantity + "</td></tr>";
                // }
                innerHTML += "</table></td>" +
                    "<td><table class=\"table table-hover table-dark table-borderless\">";
                for (let j = 0; j < product.sizes.length; j++) {
                    innerHTML += "<tr><td>" +
                        "<div class=\"row\"><div class=\"col-md-6 col-lg-6\">" +
                        "<button type=\"button\" " +
                        "class=\"btn btn-outline-danger btn-sm\" " +
                        "id=\"productDelete_" + product.id + "\" " +
                        "onclick=\"deleteProduct(\'" + product.id + "\')\">Remove</button></div>" +
                        "<div class=\"col-md-6 col-lg-6\"><button type=\"button\" " +
                        "class=\"btn btn-outline-warning btn-sm\" " +
                        "id=\"productEdit_" + product.id + "\" " +
                        "onclick=\"editProduct(\'" + product.id + "\')\">Edit</button></div>" +
                        "</div></tr></td>";
                }
                productTableBodyHTML += innerHTML;
            }
            productTableBody.html(productTableBodyHTML)
        }
    });
}

function deleteProductAddOrder(productIndex) {
    for (let i = 0; i < productsForOrder.length; i++) {
        if (i === productIndex) {
            productsForOrder.splice(i, 1);
            break;
        }
    }
    populateProductTableAddOrder();
    let add_order_total_prize = document.querySelector("#add_order_total_prize");
    let order_total_prize = 0;
    for (let i = 0; i < productsForOrder.length; i++) {
        order_total_prize = (productsForOrder[i].prize * productsForOrder[i].quantity) - productsForOrder[i].discount;
    }
    add_order_total_prize.innerHTML = order_total_prize;
}

function deleteProduct(id) {
    console.log(id)
}

function editProduct(id) {
    console.log(id)
}

function detailsOder(ordeUniqueID) {
    let orderDetailsDivOrderList = document.querySelector("#orderDetailsDivOrderList");
    let orderList = document.querySelector("#orderList");

    orderList.classList.toggle("blur", true);
    orderDetailsDivOrderList.classList.toggle("show", true);

    let orderIDOrderList = document.querySelector(("#orderIDOrderList"));
    let customerNameOrderList = document.querySelector(("#customerNameOrderList"));
    let customerPhoneNumberOrderList = document.querySelector(("#customerPhoneNumberOrderList"));
    let customerAddressOrderList = document.querySelector(("#customerAddressOrderList"));
    let customerNoteOrderList = document.querySelector(("#customerNoteOrderList"));
    let paymentStatusOrderList = document.querySelector(("#paymentStatusOrderList"));
    let paymentMethodOrderList = document.querySelector(("#paymentMethodOrderList"));
    let orderDiscountOrderList = document.querySelector(("#orderDiscountOrderList"));
    let orderFromOrderList = document.querySelector(("#orderFromOrderList"));

    for (let i = 0; i < ordersFromDB.length; i++) {
        if (parseInt(ordeUniqueID) === ordersFromDB[i].ordeUniqueID) {
            orderIDOrderList.innerHTML = ordersFromDB[i].ordeUniqueID;
            customerNameOrderList.innerHTML = ordersFromDB[i].userDao.name;
            customerPhoneNumberOrderList.innerHTML = ordersFromDB[i].userDao.phone;
            customerAddressOrderList.innerHTML = ordersFromDB[i].userDao.address;
            if (ordersFromDB[i].orderNote === null) {
                customerNoteOrderList.innerHTML = "*";
            } else {
                customerNoteOrderList.innerHTML = ordersFromDB[i].orderNote;
            }
            populateProductTableOrderDetailsOrderList(ordersFromDB[i].productDaos, ordersFromDB[i].orderID);
            if (ordersFromDB[i].paymentStatus === "Unpaid") {
                paymentStatusOrderList.style.color = "#ff0000";
            } else if (ordersFromDB[i].paymentStatus === "Paid") {
                paymentStatusOrderList.style.color = "#08e243"
            }
            paymentStatusOrderList.innerHTML = ordersFromDB[i].paymentStatus;
            paymentMethodOrderList.innerHTML = ordersFromDB[i].paymentMethod;
            orderDiscountOrderList.innerHTML = ordersFromDB[i].orderDiscount;
            orderFromOrderList.innerHTML = ordersFromDB[i].orderFrom;
            break;
        }
    }
}

function populateProductTableOrderDetailsOrderList(productDao, orderID) {
    let order_list_product_table_body = document.querySelector("#orderProductTableBodyOrderList");
    let productTableBodyHTML = "";
    if (productDao.length > 1) {
        let count = 1;
        for (let i = 0; i < productDao.length; i++) {
            let product = productDao[i];
            let innerHTML = "<tr>" +
                "<td>" + product.name + "</td>" +
                "<td>" + product.sizes[0] + "</td>" +
                "<td>" + product.quantities[0] + "</td>" +
                "<td>" + product.orderDiscount + "</td>" +
                "<td>" +
                "<button type=\"button\" class=\"btn btn-outline-danger btn-sm\" " +
                "onclick=\"deleteProductOrderList(" + product.productSizeID + ", " + orderID + ", " + count + ")\">Remove</button>" +
                "</td></tr>";
            productTableBodyHTML += innerHTML;
            count++;
        }
    } else {
        for (let i = 0; i < productDao.length; i++) {
            let product = productDao[i];
            let innerHTML = "<tr>" +
                "<td>" + product.name + "</td>" +
                "<td>" + product.sizes[0] + "</td>" +
                "<td>" + product.quantities[0] + "</td>" +
                "<td>" + product.orderDiscount + "</td>" +
                "<td>" +
                "<button type=\"button\" class=\"btn btn-outline-danger btn-sm\" >Remove</button>" +
                "</td></tr>";
            productTableBodyHTML += innerHTML;
        }
    }
    order_list_product_table_body.innerHTML = productTableBodyHTML;
}

function closeOrderDetailsDivOrderList() {
    let orderDetailsDivOrderList = document.querySelector("#orderDetailsDivOrderList");
    let orderList = document.querySelector("#orderList");
    orderList.classList.toggle("blur", false);
    orderDetailsDivOrderList.classList.toggle("show", false);
}

function deleteProductOrderList(productSizeID, orderID, tableRowNumber) {
    let url = "removeProductFromOrderList"
    let value = {
        productSizeID: productSizeID,
        orderID: orderID
    }
    $.ajax({
        type: "DELETE",
        url: url,
        data: value,
        dataType: 'json',
        success: function (returnData) {
            if (returnData === 1) {
                let orderProductTableOrderList = document.querySelector("#orderProductTableOrderList");
                for (let i = 1; i < orderProductTableOrderList.rows.length; i++) {
                    orderProductTableOrderList.deleteRow(i);
                }
                let isBreak = false;
                for (let i = 0; i < ordersFromDB.length; i++) {
                    if (ordersFromDB[i].orderID === orderID) {
                        for (let j = 0; j < ordersFromDB[i].productDaos.length; j++) {
                            if (ordersFromDB[i].productDaos[j].productSizeID === productSizeID) {
                                ordersFromDB[i].productDaos.splice(j, 1);
                                isBreak = true;
                                break;
                            }
                        }
                        if (isBreak) {
                            populateProductTableOrderDetailsOrderList(ordersFromDB[i].productDaos, orderID);
                            break;
                        }
                    }
                }
            }
        }
    });
}

function filteredDateOrderList() {
    let filteredDate = document.querySelector("#filteredDateOrderList").value;
    let url = "findOrderByDate"
    let value = {
        filteredDate: filteredDate
    }
    $.ajax({
        type: "GET",
        url: url,
        data: value,
        dataType: 'json',
        success: function (returnData) {
            if (returnData !== null) {
                ordersFromDB = returnData;
                populateOrderListTable(returnData);
            } else {
                let orderListTable = document.querySelector("#orderListTable");
                for (let i = 1; i < orderListTable.rows.length; i++) {
                    orderListTable.deleteRow(i);
                }
            }
        }
    });
}

function filteredOrderIDOrderList() {
    let filteredOrderID = document.querySelector("#filteredOrderIDOrderList").value;
    let url = "findOrderByOrderID"
    let value = {
        filteredOrderID: filteredOrderID
    }
    $.ajax({
        type: "GET",
        url: url,
        data: value,
        dataType: 'json',
        success: function (returnData) {
            if (returnData !== null) {
                ordersFromDB = returnData;
                populateOrderListTable(returnData);
            } else {
                let orderListTable = document.querySelector("#orderListTable");
                for (let i = 1; i < orderListTable.rows.length; i++) {
                    orderListTable.deleteRow(i);
                }
            }
        }
    });
}

function populateOrderListTable(orderList) {
    let orderListTable = document.querySelector("#orderListTable");
    let orderListTableBody = document.querySelector("#orderListTableBody");
    let orderListTableBodyHTML = "";
    for (let i = 1; i < orderListTable.rows.length; i++) {
        orderListTable.deleteRow(i);
    }
    for (let i = 0; i < orderList.length; i++) {
        let orderID = orderList[i].ordeUniqueID;
        let customerName = orderList[i].userDao.name;
        let customerLocation = orderList[i].userDao.location;
        let deliveryStatus = orderList[i].deliveryStatus;

        let innerHTML = "<tr>" +
            "<td>" + orderID + "</td>" +
            "<td>" + customerName + "</td>" +
            "<td>" + customerLocation + "</td>" +
            "<td>" + deliveryStatus + "</td>" +
            "<td>" +
            "<button type=\"button\" class=\"btn btn-outline-warning btn-sm\" " +
            "onclick=\"detailsOder(" + orderID + ")\">Details</button>" +
            "<button type=\"button\" class=\"btn btn-outline-danger btn-sm\" " +
            "onclick=\"cancelOrder(" + orderID + ")\">Cancel</button>"
        "</td></tr>";
        orderListTableBodyHTML += innerHTML;
    }
    orderListTableBody.innerHTML = orderListTableBodyHTML;
    console.log(orderList);
}

function addOrderOrderDiscount() {
    let add_order_discount = document.querySelector("#add_order_discount").value;
    let add_order_total_prize = parseInt(document.querySelector("#add_order_total_prize").innerText);
    if (add_order_discount.length === 0) {
        alert("Add an Order Discount First");
        document.querySelector("#add_order_discount").focus();
        return;
    }
    if (add_order_total_prize === 0) {
        alert("Add a product to the order first.");
        document.querySelector("#add_order_product_category").focus();
        return;
    }
    if (parseInt(add_order_discount) > add_order_total_prize) {
        alert("Invalid Discount");
        document.querySelector("#add_order_discount").focus();
        return;
    }
    let total_prize_after_Order_discount = add_order_total_prize - parseInt(add_order_discount);
    document.querySelector("#add_order_total_prize").innerHTML = total_prize_after_Order_discount;
    document.querySelector("#add_order_order_discount_button").disabled = true;
}

function cancelOrder(orderUniqeID) {
    let url = "cancelOrder";
    let value = {
        orderUniqeID: orderUniqeID
    }
    $.ajax({
        type: "GET",
        url: url,
        data: value,
        dataType: 'json',
        success: function (returnData) {
            if (returnData === 1) {
                for (let i = 0; i < ordersFromDB.length; i++) {
                    if (ordersFromDB[i].ordeUniqueID === parseInt(orderUniqeID)) {
                        ordersFromDB[i].deliveryStatus = "ORDER_CANCEL";
                        break;
                    }
                }
                populateOrderListTable(ordersFromDB);
            }
        }
    });
}