package fr.univtln.mapare.exceptions;

import jakarta.ws.rs.core.Response;

public abstract class MyResponse extends Response {
    public enum Status implements Response.StatusType {
        OK(200, "OK"),
        CREATED(201, "Created"),
        ACCEPTED(202, "Accepted"),
        NO_CONTENT(204, "No Content"),
        RESET_CONTENT(205, "Reset Content"),
        PARTIAL_CONTENT(206, "Partial Content"),
        MOVED_PERMANENTLY(301, "Moved Permanently"),
        FOUND(302, "Found"),
        SEE_OTHER(303, "See Other"),
        NOT_MODIFIED(304, "Not Modified"),
        USE_PROXY(305, "Use Proxy"),
        TEMPORARY_REDIRECT(307, "Temporary Redirect"),
        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        PAYMENT_REQUIRED(402, "Payment Required"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not Found"),
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
        NOT_ACCEPTABLE(406, "Not Acceptable"),
        PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
        REQUEST_TIMEOUT(408, "Request Timeout"),
        CONFLICT(409, "Conflict"),
        GONE(410, "Gone"),
        LENGTH_REQUIRED(411, "Length Required"),
        PRECONDITION_FAILED(412, "Precondition Failed"),
        REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
        REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
        UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
        REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
        EXPECTATION_FAILED(417, "Expectation Failed"),
        TOO_EARLY(425, "Too Early"),
        PRECONDITION_REQUIRED(428, "Precondition Required"),
        TOO_MANY_REQUESTS(429, "Too Many Requests"),
        REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        NOT_IMPLEMENTED(501, "Not Implemented"),
        BAD_GATEWAY(502, "Bad Gateway"),
        SERVICE_UNAVAILABLE(503, "Service Unavailable"),
        GATEWAY_TIMEOUT(504, "Gateway Timeout"),
        HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
        NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

        private final int code;
        private final String reason;
        private final Response.Status.Family family;

        Status(int statusCode, String reasonPhrase) {
            this.code = statusCode;
            this.reason = reasonPhrase;
            this.family = Response.Status.Family.familyOf(statusCode);
        }

        public Response.Status.Family getFamily() {
            return this.family;
        }

        public int getStatusCode() {
            return this.code;
        }

        public String getReasonPhrase() {
            return this.toString();
        }

        public String toString() {
            return this.reason;
        }

        public static Status fromStatusCode(int statusCode) {
            Status[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                Status s = var1[var3];
                if (s.getStatusCode() == statusCode) {
                    return s;
                }
            }

            return null;
        }

        public enum Family {
            INFORMATIONAL,
            SUCCESSFUL,
            REDIRECTION,
            CLIENT_ERROR,
            SERVER_ERROR,
            OTHER;

            Family() {
            }

            public static Family familyOf(int statusCode) {
                switch (statusCode / 100) {
                    case 1:
                        return INFORMATIONAL;
                    case 2:
                        return SUCCESSFUL;
                    case 3:
                        return REDIRECTION;
                    case 4:
                        return CLIENT_ERROR;
                    case 5:
                        return SERVER_ERROR;
                    default:
                        return OTHER;
                }
            }
        }
    }
}
