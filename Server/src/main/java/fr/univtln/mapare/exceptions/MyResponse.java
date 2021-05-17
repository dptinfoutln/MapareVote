package fr.univtln.mapare.exceptions;

import jakarta.ws.rs.core.Response;

/**
 * The type My response.
 */
public abstract class MyResponse extends Response {
    /**
     * The enum Status.
     */
    public enum Status implements Response.StatusType {
        /**
         * Ok status.
         */
        OK(200, "OK"),
        /**
         * Created status.
         */
        CREATED(201, "Created"),
        /**
         * Accepted status.
         */
        ACCEPTED(202, "Accepted"),
        /**
         * The No content.
         */
        NO_CONTENT(204, "No Content"),
        /**
         * The Reset content.
         */
        RESET_CONTENT(205, "Reset Content"),
        /**
         * The Partial content.
         */
        PARTIAL_CONTENT(206, "Partial Content"),
        /**
         * The Moved permanently.
         */
        MOVED_PERMANENTLY(301, "Moved Permanently"),
        /**
         * Found status.
         */
        FOUND(302, "Found"),
        /**
         * The See other.
         */
        SEE_OTHER(303, "See Other"),
        /**
         * The Not modified.
         */
        NOT_MODIFIED(304, "Not Modified"),
        /**
         * The Use proxy.
         */
        USE_PROXY(305, "Use Proxy"),
        /**
         * The Temporary redirect.
         */
        TEMPORARY_REDIRECT(307, "Temporary Redirect"),
        /**
         * The Bad request.
         */
        BAD_REQUEST(400, "Bad Request"),
        /**
         * Unauthorized status.
         */
        UNAUTHORIZED(401, "Unauthorized"),
        /**
         * The Payment required.
         */
        PAYMENT_REQUIRED(402, "Payment Required"),
        /**
         * Forbidden status.
         */
        FORBIDDEN(403, "Forbidden"),
        /**
         * The Not found.
         */
        NOT_FOUND(404, "Not Found"),
        /**
         * The Method not allowed.
         */
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
        /**
         * The Not acceptable.
         */
        NOT_ACCEPTABLE(406, "Not Acceptable"),
        /**
         * The Proxy authentication required.
         */
        PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
        /**
         * The Request timeout.
         */
        REQUEST_TIMEOUT(408, "Request Timeout"),
        /**
         * Conflict status.
         */
        CONFLICT(409, "Conflict"),
        /**
         * Gone status.
         */
        GONE(410, "Gone"),
        /**
         * The Length required.
         */
        LENGTH_REQUIRED(411, "Length Required"),
        /**
         * The Precondition failed.
         */
        PRECONDITION_FAILED(412, "Precondition Failed"),
        /**
         * The Request entity too large.
         */
        REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
        /**
         * The Request uri too long.
         */
        REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
        /**
         * The Unsupported media type.
         */
        UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
        /**
         * The Requested range not satisfiable.
         */
        REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
        /**
         * The Expectation failed.
         */
        EXPECTATION_FAILED(417, "Expectation Failed"),
        /**
         * The Too early.
         */
        TOO_EARLY(425, "Too Early"),
        /**
         * The Precondition required.
         */
        PRECONDITION_REQUIRED(428, "Precondition Required"),
        /**
         * The Too many requests.
         */
        TOO_MANY_REQUESTS(429, "Too Many Requests"),
        /**
         * The Request header fields too large.
         */
        REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
        /**
         * The Internal server error.
         */
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        /**
         * The Not implemented.
         */
        NOT_IMPLEMENTED(501, "Not Implemented"),
        /**
         * The Bad gateway.
         */
        BAD_GATEWAY(502, "Bad Gateway"),
        /**
         * The Service unavailable.
         */
        SERVICE_UNAVAILABLE(503, "Service Unavailable"),
        /**
         * The Gateway timeout.
         */
        GATEWAY_TIMEOUT(504, "Gateway Timeout"),
        /**
         * The Http version not supported.
         */
        HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
        /**
         * The Network authentication required.
         */
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

        /**
         * From status code status.
         *
         * @param statusCode the status code
         * @return the status
         */
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

        /**
         * The enum Family.
         */
        public enum Family {
            /**
             * Informational family.
             */
            INFORMATIONAL,
            /**
             * Successful family.
             */
            SUCCESSFUL,
            /**
             * Redirection family.
             */
            REDIRECTION,
            /**
             * Client error family.
             */
            CLIENT_ERROR,
            /**
             * Server error family.
             */
            SERVER_ERROR,
            /**
             * Other family.
             */
            OTHER;

            Family() {
            }

            /**
             * Family of family.
             *
             * @param statusCode the status code
             * @return the family
             */
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
