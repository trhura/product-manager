package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Pricing;
import models.Product;
import play.mvc.*;
import scala.Option;
import scala.collection.Iterator;
import scala.collection.immutable.List;

/**
 * Created with IntelliJ IDEA.
 * User: trhura
 * Date: 11/23/13
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductController extends Controller {

    final static Option<Product> None = scala.Option.apply(null);
    final static JsonNodeFactory factory = JsonNodeFactory.instance;

    /* Index page*/
    public static Result index () {
        return ok(views.html.index.render());
    }

    /* GET API: return Json by product id */
    public static Result show(Long id) {
        Option<Product> productOption = Product.byId(id);
        if (productOption == None) return notFound();
        Product product = productOption.get();

        JsonNode json = getJsonFromProduct(product);
        return ok(json);
    }

    /* POST API: update Product */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {
        Option<Product> productOption = Product.byId(id);
        if (productOption == None) return notFound();
        Product product = productOption.get();

        JsonNode jsonBody = request().body().asJson();
        String title = jsonBody.get("title").asText();
        Double price = jsonBody.get("pricing").get("price").asDouble();

        updateProduct(product, title, price);
        return ok();
    }

    /* Helper function */
    private static void updateProduct(Product product, String title, Double price) {
        Pricing pricing = new Pricing(
                product.pricing().cost(),
                price,
                product.pricing().promo_price(),
                product.pricing().savings(),
                product.pricing().on_sale()
        );

        Product updatedProduct = new Product(
                product._id(),
                product.id(),
                title,
                pricing
        );

        Product.update (updatedProduct);
    }

    /* API: get a list of possible completions for product id */
    public static Result completeId(Long id) {
        ArrayNode completions = factory.arrayNode();
        List<Product> products = Product.all();
        Iterator iter = products.iterator();
        String string_id = String.valueOf(id);

        while (iter.hasNext()) {
            Product product = (Product) iter.next();
            Long product_id = product.id();
            String string_product_id = String.valueOf(product_id);

            if ( string_id.length() >= string_product_id.length()) {
                continue;
            }

            String substring_product_id = string_product_id.substring(0, string_id.length());
            if (string_id.equals(substring_product_id)) {
                completions.add(string_product_id);
            }
        }

        return ok(completions);
    }

    /* Helper function */
    private static JsonNode getJsonFromProduct(Product product) {
        ObjectNode pricingNode = factory.objectNode();
        pricingNode.put ("cost", factory.numberNode(product.pricing().cost()));
        pricingNode.put ("price", factory.numberNode(product.pricing().price()));
        pricingNode.put ("promo_price", factory.numberNode(product.pricing().promo_price()));
        pricingNode.put ("on_sale", factory.numberNode(product.pricing().on_sale()));

        ObjectNode baseNode = factory.objectNode();
        baseNode.put("id", factory.numberNode(product.id()));
        baseNode.put("title", factory.textNode(product.title()));
        baseNode.put("pricing", pricingNode);

        return baseNode;
    }
}
