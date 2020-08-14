import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HttpClientMain {

	public static CloseableHttpClient client;
	public static final String CATEGORY = "SHOES";
	public static final String fileNameSQL = CATEGORY+".sql"; 
	
	/**
	 *   2020-08-13 작성
	 *   Java Web Crawling (Apache HttpComponents)
	 *   참고 사이트 : https://cofs.tistory.com/260
	 *   https://inyl.github.io/programming/2017/09/14/http_component.html
	 *   목적 - MatchesFashion 프로젝트 진행을 위해 웹 크롤링으로 데이터 획득
	 *   로그인 시도 실패 429 에러 발생 -> Jsoup으로 그냥 Parsing만 진행..
	 *   Jsoup으로 URL 변경으로만 데이터 획득 완료
	 *   결과물 src/imgs, sql
	 *   원본 사이트 폰트 BlairITC --- https://www.fontsplace.com/blairitc-tt-bold-free-font-download.html
	 *   Berthold  ---- https://fontsgeek.com/fonts/Berthold-Akzidenz-Grotesk-Super
	 */
	public static void main(String[] args) {
		try {
			Document doc = Jsoup.connect("https://www.matchesfashion.com/en-kr/womens/shop/"+CATEGORY).get();
			Elements contents = doc.select("img.lazy");
			List<String> list = new ArrayList<String>();
			List<ProductDTO> _list = new ArrayList<ProductDTO>();
			// get ImagePath and ProductCode
			for(int i=0;i<contents.size();i++) {
				ProductDTO dto = new ProductDTO();
				String productCode = contents.get(i).attr("data-product-code");
				String temp = contents.get(i).attr("data-original");
				int length = Integer.parseInt(contents.get(i).attr("data-image-length"));
				temp = "http://"+temp.substring(2, temp.length());
				String productImgPath = temp.substring(temp.lastIndexOf("/"), temp.length());
				//list.add(temp);
				String zoom = temp.replace("large", "zoom");
				//list.add(zoom);
				String smallsize = temp.replace("_large", "");
				smallsize = smallsize.replace("/product/", "/product/66/");
				list.add(smallsize);
				for(int q = 1;q<length;q++) {
					temp = temp.replace("_"+q+"_", "_"+(q+1)+"_");
					//list.add(temp);
					zoom = temp.replace("large", "zoom");
					//list.add(zoom);
					smallsize = temp.replace("_large", "");
					smallsize = smallsize.replace("/product/", "/product/66/");
					list.add(smallsize);
				}
				dto.setProductCode(productCode);
				dto.setProductImgPath("./img/product/shoes"+productImgPath);
				//System.out.println(dto.getProductImgPath());
				dto.setProductCategory(CATEGORY);
				dto.setProductImageLength(length);
				_list.add(dto);
			}
			Elements content2 = doc.select("div.lister__item__title");
			// getTitle   
			for(int i=0;i<content2.size();i++) {
				ProductDTO dto = _list.get(i);
				dto.setProductName(content2.get(i).text());
			}
			Elements content3 = doc.select("div.lister__item__details");
			for(int i=0;i<content3.size();i++) {
				ProductDTO dto = _list.get(i);
				dto.setProductDescription(content3.get(i).text());
			}
			
			Elements content4 = doc.select("div.lister__item__price");
			for(int i=0;i<content4.size();i++) {
				ProductDTO dto = _list.get(i);
				dto.setProductPrice(content4.get(i).text());
			}
//			saveQuery(_list);
			for(int i=0;i<list.size();i++) {
				System.out.println("start to save Image ===> " + i + "   --- length ===> "+list.size() );
				saveImage(list.get(i));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveQuery(List<ProductDTO> list) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\kyyet\\git\\MatchesFashionCrawling\\sql\\"+fileNameSQL));
			for(int i=0;i<list.size();i++) {
				writer.write(list.get(i).getQuery()+"\n");
			}
			System.out.println("saved Query ===> " + fileNameSQL);
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveImage(String url) {
		try {
			String fileName = url.substring(url.lastIndexOf("/"), url.length());
			URL _url = new URL(url);
			InputStream in = _url.openStream();
			OutputStream out = new FileOutputStream("C:\\Users\\kyyet\\git\\MatchesFashionCrawling\\src\\imgs\\"+fileName);
			while(true) {
				int data = in.read();
				if(data == -1) break;
				out.write(data);
			}
			in.close();
			out.close();
			System.out.println("saved Query ===> " + fileName);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void crawling() {
		client = HttpClientBuilder.create().build();
		
//		String url = "https://www.matchesfashion.com/en-kr/login/j_spring_security_check";
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("j_username", "kyyeto9984@naver.com");
//		param.put("j_password", "!Q@W#E1q2w3e");
//		param.put("_remember-me", "on");
//		param.put("remember-me", "true");
//		param.put("CSRFToken", "daaa8195-fff3-4e85-8d14-cbb047422d01");
		String url = "https://www.matchesfashion.com/en-kr/womens/shop/shoes";
		HttpClientMain main = new HttpClientMain();
		String text = main.get(url, null);
		System.out.println(text);
		try {			
			client.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String get(String url, Map<String, String> params) {
		return get(url, params, "UTF-8");
	}
	
	public String post(String url, Map<String, String> params) {
		return post(url, params, "UTF-8");
	}
	
	public String get(String url, Map<String, String> params, String encoding) {
		try {
			List<NameValuePair> paramList = convertParam(params);
			HttpGet get = new HttpGet(url + "?" + URLEncodedUtils.format(paramList, encoding));
			System.out.println("GET ===> " + get.getURI());
			ResponseHandler<String> rh = new BasicResponseHandler();
			String execute = client.execute(get, rh);
			return execute;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	public String post(String url, Map<String, String> params, String encoding) {
		try {
			List<NameValuePair> paramList = convertParam(params);
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(paramList, encoding));
			
			System.out.println("Post ===> " + post.getURI());
			
			ResponseHandler<String> rh = new BasicResponseHandler();
			String execute = client.execute(post, rh);
			return execute;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	private List<NameValuePair> convertParam(Map<String, String> params){
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		Iterator<String> keys = params.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
		}
		return paramList;
	}
}
