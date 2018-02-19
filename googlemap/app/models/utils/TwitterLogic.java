package models.utils;

import play.libs.oauth.OAuth;
import play.libs.oauth.OAuth.ConsumerKey;
import play.libs.oauth.OAuth.ServiceInfo;

public class TwitterLogic {

	public static final ConsumerKey KEY = new ConsumerKey("","");

	public static final ServiceInfo SERVICE_INFO = new ServiceInfo("https://api.twitter.com/oauth/request_token",
			"https://api.twitter.com/oauth/access_token", "https://api.twitter.com/oauth/authenticate", KEY);

	public static final OAuth TWITTER = new OAuth(SERVICE_INFO);

}
