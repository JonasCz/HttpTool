package jonas.tool.httpRequestCreator.Constants;

public class ResponseBodyViewTypes {
	//todo: replace this with an enum
	public static final String[] TYPES = {
		"Raw text",
		"Raw text, pretty print (Code, HTML Source, JSON..) Slow for large files !",
		"Rendered HTML, plain",
		"Rendered HTML, with formatting / images / assets",
		"Image (PNG/GIF/Bitmap binary data)",
		"Hex dump",
		"Raw text, base64 decoded",
	};
}
