package jonas.tool.httpRequestCreator.Constants;

public class ResponseBodyViewTypes {
	//todo: replace this with an enum
	public static final String[] TYPES = {
		"Raw text",
		"Raw text, pretty print (Code, HTML Source, JSON..) Slow for very large files !",
		"Rendered HTML, plain (Block additional network requests from the page)",
		"Rendered HTML, with assets / CSS / images / AJAX (loaded from network)",
		"Rendered HTML, with assets.., Enable interception of network requests from the page",
		"Image (PNG/GIF/WEBP/A-PNG/Bitmap binary data)",
		"Hex dump",
		"Raw text, base64 decoded",
	};
}
