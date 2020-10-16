package com.elcom.gateway.restcore;

public enum AcceptType {
    NotSet("NotSet"),
    Json("application/json"),
    Xml("application/xml");

    String _value;

    AcceptType(String p) {
        _value = p;
    }

    public String getDescription() {
        return _value;
    }
}
