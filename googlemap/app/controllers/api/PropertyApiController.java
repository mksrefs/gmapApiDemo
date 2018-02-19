package controllers.api;

import play.mvc.Result;
import java.util.List;

import models.BuyProperty;
import play.libs.Json;
import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class PropertyApiController extends Controller {
	/**
	 * 表示範囲の緯度経度から物件情報のjsonを返す
	 * 
	 * @param latFrom
	 * @param latTo
	 * @param lngFrom
	 * @param lngTo
	 * @return json
	 */
	public Result searchBoundingbox(Double latFrom, Double latTo, Double lngFrom, Double lngTo) {
		if (latFrom == null || latTo == null || lngFrom == null || lngTo == null)
			return ok(Json.toJson(""));

		if (latFrom.isNaN() || latTo.isNaN() || lngFrom.isNaN() || lngTo.isNaN())
			return ok(Json.toJson(""));

		List<BuyProperty> buyPropertyList = BuyProperty.findByRage(latFrom, latTo, lngFrom, lngTo);
		if (buyPropertyList.isEmpty())
			return ok(Json.toJson(""));

		return ok(Json.toJson(buyPropertyList));
	}

}
