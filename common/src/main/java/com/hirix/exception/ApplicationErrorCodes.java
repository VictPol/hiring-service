package com.hirix.exception;

public enum ApplicationErrorCodes {
    SQL_ERROR(11),
    BAD_REQUEST_USER_CREATE(66),
    USER_NOT_FOUND(44),
    NO_REPLY_FROM_THIS_RESOURCE(22),
    ID_IS_NOT_LONG(33),
    NO_ENTITY_WITH_SUCH_ID(55),
    POOR_INFORMATION_TO_CREATE_ENTITY(77),
    FATAL_ERROR(1);

    public int getCodeId() {
        return codeId;
    }

    private int codeId;

    ApplicationErrorCodes(int codeId) {
        this.codeId = codeId;
    }
}
