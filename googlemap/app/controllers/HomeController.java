package controllers;

import play.mvc.Result;

import java.util.Optional;

import play.libs.oauth.OAuth.RequestToken;
import play.mvc.*;
import views.html.*;

public class HomeController extends Controller {

	/**
	 * トップページを表示
	 * 
	 * @return
	 */
	public Result index() {
		return ok(index.render());
	}
}
