package com.sleepy.zeo.springboot.data;

import static javax.servlet.http.HttpServletResponse.*;

public class Constants {
    public static final String UPLOAD_DIR = "springboot-upload";
    public static final String DOWNLOAD_DIR = "springboot-upload";

    public static final String CATALINA_MIME_BOUNDARY = "CATALINA_MIME_BOUNDARY";

    public static final String HTTP_HEADER_CONTENT_TYPE_KEY = "Content-Type";
    public static final String HTTP_HEADER_CONTENT_LENGTH_KEY = "Content-Length";
    public static final String HTTP_HEADER_RANGE_KEY = "Range";
    public static final String HTTP_HEADER_ACCEPT_RANGES_KEY = "Accept-Ranges";
    public static final String HTTP_HEADER_CONTENT_RANGE_KEY = "Content-Range";
    public static final String HTTP_HEADER_ETAG_KEY = "ETag";
    public static final String HTTP_HEADER_LAST_MODIFIED_KEY = "Last-Modified";
    public static final String HTTP_HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    public static final int HTTP_CODE_OK = SC_OK;
    public static final int HTTP_CODE_PARTIAL_CONTENT = SC_PARTIAL_CONTENT;
    public static final int HTTP_CODE_REQUESTED_RANGE_NOT_SATISFIABLE = SC_REQUESTED_RANGE_NOT_SATISFIABLE;
    public static final int HTTP_CODE_NOT_FOUND = SC_NOT_FOUND;

    public static final boolean ENABLE_VERIFY_FILTER = false;

}
