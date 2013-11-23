package controllers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import models.Product;
import play.mvc.*;
import scala.Option;

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


    public static Result show(Long id) {
        Option<Product> productOption = Product.findOneById(id);
        if (productOption == None) return notFound();

        Product product = productOption.get();
        if (request().accepts("text/html")) {
            return ok(views.html.product_show.render(product));
        }

        if (request().accepts(("application/json"))) {
            JsonNode json = getJson(product);
            return ok(json);
        }

        return notFound();
    }


    private static JsonNode getJson(Product product) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;

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
