package controllers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;
import javax.inject.Inject;

import com.google.common.base.Strings;

import models.utils.TwitterLogic;
import play.Logger;
import play.libs.oauth.OAuth.OAuthCalculator;
import play.libs.oauth.OAuth.RequestToken;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

public class TwitterController extends Controller {

	@Resource
	protected TwitterLogic twitterLogic;

	public final WSClient ws;

	@Inject
	private TwitterController(WSClient ws) {
		this.ws = ws;
	}

	/**
	 * 
	 * ログイン処理
	 * 
	 * @return
	 */
	public Result login() {
		String verifier = request().getQueryString("oauth_verifier");
		if (Strings.isNullOrEmpty(verifier)) {
			String url = routes.TwitterController.login().absoluteURL(request());
			RequestToken requestToken = twitterLogic.TWITTER.retrieveRequestToken(url);
			saveSessionTokenPair(requestToken);
			return redirect(twitterLogic.TWITTER.redirectUrl(requestToken.token));
		} else {
			RequestToken requestToken = getSessionTokenPair().get();
			RequestToken accessToken = twitterLogic.TWITTER.retrieveAccessToken(requestToken, verifier);
			saveSessionTokenPair(accessToken);
			return redirect(routes.TwitterController.getTwitterName());
		}
	}

	/**
	 * 
	 * ログアウト処理
	 * 
	 * @return
	 */
	public Result logout() {
		session().clear();
		return redirect(routes.HomeController.index());
	}

	/**
	 * 
	 * セッショントークンを保存する
	 * 
	 * @param requestToken
	 */
	public void saveSessionTokenPair(RequestToken requestToken) {
		session("token", requestToken.token);
		session("secret", requestToken.secret);
	}

	/**
	 * 
	 * セッショントークンを取得する
	 * 
	 * @return
	 */
	public static final Optional<RequestToken> getSessionTokenPair() {
		if (session().containsKey("token"))
			return Optional.ofNullable(new RequestToken(session("token"), session("secret")));
		return Optional.empty();
	}

	/**
	 * 
	 * twitterにauth認証した後、ユーザー情報を取得
	 * 
	 * @return
	 */
	public CompletionStage<Result> getTwitterName() {
		Optional<RequestToken> sessionTokenPair = getSessionTokenPair();
		CompletionStage<Result> resultRedirect = CompletableFuture
				.completedFuture(redirect(routes.HomeController.index()));
		if (sessionTokenPair.isPresent()) {
			// apiのjson結果をstringにしてsessionにsetする
			CompletableFuture<String> name = ws.url("https://api.twitter.com/1.1/account/verify_credentials.json")
					.sign(new OAuthCalculator(TwitterLogic.KEY, sessionTokenPair.get())).get().thenApply(result -> {
						return result.asJson().findPath("name").toString().replaceAll("\"", "");
					}).toCompletableFuture();
			// sessionにusernameが入らなくても、top画面にredirectさせる
			try {
				session("name", name.get());
			} catch (InterruptedException | ExecutionException e) {
				Logger.error(e.getMessage(), e);
				return resultRedirect;
			}
			return resultRedirect;
		}
		return resultRedirect;
	}

}
