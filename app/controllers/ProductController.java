package controllers;

import play.mvc.*;

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

    public static Result show(Long id) {
        //return ok(views.html.product_show.render(id));
        return TODO; //ok(views.html.)
    }
}
