$(document).ready(function(){
    function loadProduct (json) {
        $('#id').val(json.id);
        $('#title').val(json.title);
        $('#price').val(json.pricing.price);
        $('#cost').val(json.pricing.cost);
    }

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
