package com.rainingclouds.hatchttp.exception;

/**
 * Enum for error codes related with this library
 * Created by akshay on 7/22/14.
 */
public enum HatcHttpErrorCode {
    REQUEST_PREPARATION_EXCEPTION,
    NO_DATA_CONNECTION,
    MAX_RETRIES_FOR_CONTACTING_SERVER,
    REQUEST_TIMEOUT,
    UNSUPPORTED_ENCODING,
    CLIENT_PROTOCOL_EXCEPTION,
    SOCKET_EXCEPTION,
    WEB_APP_EXCEPTION,
    JSON_EXCEPTION,
    UNKNOWN
}
