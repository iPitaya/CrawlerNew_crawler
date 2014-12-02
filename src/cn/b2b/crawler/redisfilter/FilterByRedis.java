package cn.b2b.crawler.redisfilter;

import java.util.Date;

import cn.b2b.crawler.resultdata.UJedis;

public class FilterByRedis {
	
	public String isUrlInRedis(String url) {
		String val = UJedis.get("mysql:" + url);
		return val;
	}
	
	public void UrlInsertRedis(String key, String value) {
		UJedis.set("mysql:" + key, value);
	}
	
	public static void main(String args[]){
		
	}
}
