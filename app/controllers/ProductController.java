package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

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

    public static Result index () {
        return ok(views.html.index.render());
    }

    final static Option<Product> None = scala.Option.apply(null);
    final static JsonNodeFactory factory = JsonNodeFactory.instance;

    public static Result show(Long id) {
        Option<Product> productOption = Product.byId(id);
        if (productOption == None) return notFound();
        Product product = productOption.get();

        if (request().accepts(("application/json"))) {
            JsonNode json = getJsonFromProduct(product);
            return ok(json);
        }

        if (request().accepts("text/html")) {
            return ok(views.html.product_show.render(product));
        }

        return notFound();
    }

    public static Result completeId(Long id) {
        ArrayNode completions = factory.arrayNode();
        List<Product> products = Product.all();
        Iterator iter = products.iterator();
        String string_id = String.valueOf(id);

        while (iter.hasNext()) {
            Product product = (Product) iter.next();
            Long product_id = product.id();
            String string_product_id = String.valueOf(product_id);

            if ( string_id.length() > string_product_id.length()) {
                continue;
            }

            String substring_product_id = string_product_id.substring(0, string_id.length());
            if (string_id.equals(substring_product_id)) {
                completions.add(string_product_id);
            }
        }

        return ok(completions);
    }

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
