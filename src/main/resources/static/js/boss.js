//this variables are for add order page
let add_to_order_button_click_count = 0;
let product_display_html = ''
let siderbar_first = document.querySelector(".sidebar_first");
let ip_img_input = document.getElementById('ip_img_input');
let inventory_product_submit = document.querySelector('#ip_submit_button');
//this variabls are main content og navbar clicks
let general_body = document.querySelector('.general_body');
let add_order_body = document.querySelector('.add_order_body');
let add_inventory = document.querySelector('.add_inventory');
let invertory_list = document.querySelector('.watch_inventory');
//this variables represent navbar
let sidebar_add_order = document.querySelector('#sidebar_add_order');
let sidebar_order_list = document.querySelector('#sidebar_order_list');
let sidebar_add_inventory = document.querySelector('#sidebar_add_inventory');
let sidebar_inventory_list = document.querySelector('#sidebar_inventory_list');



// $('.sidebar_first').click(function(){
//     $('.sidebar_first_show').toggleClass("show");
// });

// $('.sidebar_fourth').click(function(){
//     $('.sidebar_fourth_show').toggleClass("show");
// });

// $('.sidebar_first_show li a').click(function(){
//     $('.general_body').toggleClass("hide", true);
//     $('.add_inventory').toggleClass("show", false);
//     $('.add_order_body').toggleClass("show", true);
// })

// $('.sidebar_fourth_show li a').click(function() {
//     $('.general_body').toggleClass("hide", true);
//     $('.add_order_body').toggleClass("show", false);
//     $('.add_inventory').toggleClass("show", true);
//     // $('.watch_inventory').toggleClass("show", true);
// })



// $('.product_add').click(function(){
//     add_to_order_button_click_count++;
//     let c_name = document.getElementById('customer_name_input').value;
//     let c_phone = document.getElementById('customer_number_input').value;
//     let c_address = document.getElementById('customer_address_input').value;
//     let c_location = document.getElementById('customer_location_select').value;
//     let product_display = document.querySelector('.product_display');
//     let products = document.getElementById('customer_buying_products_name_select').value;
//     let product_quantity = document.getElementById('customer_buying_products_quantity_select').value;
//     let product_discount = document.getElementById('product_discounts_input').value;
//     if (c_name !== '' && c_phone !== '' && c_address !== '' && c_location !== '' && products !== '' && product_quantity !== '' && product_discount !== ''){
//         console.log(window);
//         product_display.style.opacity = "1";
//         product_display.style.gridTemplateColumns = "70% 15% 15%";
//         product_display_html += '<div class=\'products' + add_to_order_button_click_count + '\'>' + products + '</div>' + 
//         '<div class=\'products_prize' + add_to_order_button_click_count + '\'>' + (1200 - product_discount) + '</div>' + 
//         '<div class=\'product_cancel'+ add_to_order_button_click_count +'\' style=\'color: red; cursor: pointer\' onclick={product_cancel_clicked(\'' + add_to_order_button_click_count + '\')}> Cancel </div>';
//         product_display.innerHTML = product_display_html;
//     } else{
//         alert('Please insert the inputs')
//     }
// });

//this eventlisteners are for navbar eventlisteners
sidebar_add_inventory.addEventListener('click', function(){
    displayRelatedDashboard(add_inventory);
});

sidebar_add_order.addEventListener('click', function(){
    displayRelatedDashboard(add_order_body);
});

sidebar_inventory_list.addEventListener('click', function(){
    displayRelatedDashboard(invertory_list);
});

// ip_img_input.addEventListener('change', (event) => {
//     let img_input = event.target.files[0];
//     let inventory_product_image = document.querySelector('.inventory_product_image');
//     let ip_image_show = document.querySelector('.ip_image_show');
    
//     var reader = new FileReader();
//     reader.onload = function(e) {
//         ip_image_show.width = inventory_product_image.clientWidth;
//         ip_image_show.height = inventory_product_image.clientHeight;
//         ip_image_show.src = e.target.result;
//     }
//     reader.readAsDataURL(img_input);
// });


// inventory_product_submit.addEventListener('click', () => {
//     let ip_cat = parseInt(document.querySelector('#ip_cat_input').value);
//     let ip_name = document.querySelector('#ip_name_input').value;
//     let ip_desc = document.querySelector('#ip_desc_input').value;
//     let ip_size = document.querySelector('#ip_size_input').value;
//     let ip_quantity = parseInt(document.querySelector('#ip_quantity_input').value);
//     let ip_prize = parseInt(document.querySelector('#ip_prize_input').value);
//     let ip_img = document.querySelector('#ip_img_input');
    
//     if (ip_name && ip_cat && ip_desc && ip_size && ip_quantity && ip_prize && ip_img.files[0]){
//         $.ajax('/boss/inventory_submit', {
//                 type: 'POST',  // http method
//                 data: { myData: 'This is my data.' },  // data to submit
//                 success: function (data, status, xhr) {
//                     // $('p').append('status: ' + status + ', data: ' + data);
//                     console.log('request come back');
//                 },
//                 error: function (jqXhr, textStatus, errorMessage) {
//                         // $('p').append('Error' + errorMessage);
//                         console.log('request come back unsuccessfully');
//                 }
//             });
//     }
//     // let data = document.querySelector('#inventory_product_form').serialize();
//     // // let data = document.querySelector('#inventory_product_form');
//     // let modelData = new FormData(data);
    
//     // $.ajax('/jquery/submitData', {
//     //     type: 'POST',  // http method
//     //     data: { myData: 'This is my data.' },  // data to submit
//     //     success: function (data, status, xhr) {
//     //         $('p').append('status: ' + status + ', data: ' + data);
//     //     },
//     //     error: function (jqXhr, textStatus, errorMessage) {
//     //             $('p').append('Error' + errorMessage);
//     //     }
//     // });
  
// });

//functions
function displayRelatedDashboard(clicked_navbar){
    if(clicked_navbar.classList[0] === "add_inventory"){
        displayAddInventoryDashboard();
    } else if(clicked_navbar.classList[0] === 'add_order_body'){
        displayAddOrderBodyDashboard();
    } else if(clicked_navbar.classList[0] === 'watch_inventory'){
        displayInventoryListDashboard();
    }
}

function displayAddInventoryDashboard(){
    add_inventory.classList.add('show');
    general_body.classList.add("hide");
    add_order_body.classList.remove('show');
    invertory_list.classList.remove('show');
}

function displayAddOrderBodyDashboard(){
    add_order_body.classList.add('show');
    general_body.classList.add("hide");
    add_inventory.classList.remove('show');
    invertory_list.classList.remove('show');
}

function displayInventoryListDashboard(){
    invertory_list.classList.add('show');
    general_body.classList.add("hide");
    add_inventory.classList.remove('show');
    add_order_body.classList.remove('show');
}

function product_cancel_clicked(id){
    let name = document.querySelector('.products' + id);
    let name_value = name.innerHTML;
    let prize = document.querySelector('.products_prize' + id);
    let prize_value = prize.innerHTML;
    let cancel = document.querySelector('.product_cancel' + id);
    let cancel_value = cancel.innerHTML;
    let product_display = document.querySelector('.product_display');
    name.remove();
    prize.remove();
    cancel.remove();
    product_display_html = product_display.innerHTML;
    console.log(window)
}

function boss_dashboard_clicked(){
    console.log('boss_dashboard_clicked');
    window.location.replace("admin/dashboard")
    /*let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            window.location.replace("admin/dashboard");
        }
    };
    xhttp.open("Get", window.location.pathname + "/dashboard", true);
    xhttp.send();*/
}

function boss_web_setting_clicked(){
    console.log('boss_web_setting_clicked');
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            window.location.replace("websetting");
        }
    };
    xhttp.open("Get", window.location.pathname + "websetting", true);
    xhttp.send();
}