$(document).ready(function(){
    function loadProduct (json) {
        $('#id').val(json.id);
        $('#title').val(json.title);
        $('#price').val(json.pricing.price);
        $('#cost').val(json.pricing.cost);
    }

    // FIXME: use Route.ProductController instead of hard coded string
    $('#product_id').typeahead({
        name: 'products',
         remote: {
             url: 'product/helper/complete/',
             replace: function (url) {
                 var id = $('#product_id').val();
                 return url + id;
             }
         },
        cache: false,
        limit: 10
    });

    $('.save-on-change').keypress(function(e) {
        if(e.which == 13) {
            id = $('#id').val();
            title = $('#title').val();
            price = $('#price').val();
            cost = $('#cost').val();

            if (!id) {
                console.warn("Empty Id.");
                return;
            }

            // Title starts with letter
            if (! /^[a-z].*/i.test(title)) {
                alert("Title must start with a letter.");
                return;
            }

            if(!$.isNumeric(price)) {
                alert("Price must be a numeric.");
                return;
            }

            price = parseFloat(price);
            cost = parseFloat(cost);

            if(price <= cost)  {
                alert("Price must be larger than cost.");
                return;
            }

            product = {
                "title": title,
                "pricing": {
                    "price": price
                }
            };

            $.ajax({
                type: "POST",
                url: 'product/' + id,
                data: JSON.stringify(product),
                contentType: "application/json; charset=utf-8",
                success: function() {
                    console.log("Product updated successfully.");
                    alert("Product updated successfully.");
                }
            });
        }
    });

    $('#product_id').keypress(function(e) {
        if(e.which == 13) {
            var id = $('#product_id').val();
            if (!$.isNumeric(id)){
                alert("Product id must be a valid number, containing"
                      + " only 1- 9.");
                return;
            }

            // FIXME: use Route.ProductController instead of hard coded string
            $.getJSON('product/' + id)
                .done(function(json){
                    console.log( "Request successful: " + json);
                    loadProduct(json);
                })

                .fail(function(jqxhr, textStatus, error) {
                    alert(error);
                });
        }
    });
});
