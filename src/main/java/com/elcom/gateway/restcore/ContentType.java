package com.elcom.gateway.restcore;

public enum ContentType {
    NotSet("None"),
    Json("application/json;"),
    Xml("application/xml;");

    String _value;

    ContentType(String p) {
        _value = p;
    }

    public String getDescription() {
        return _value;
    }
}
