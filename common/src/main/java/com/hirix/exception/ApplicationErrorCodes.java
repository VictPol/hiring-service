package com.hirix.exception;

public enum ApplicationErrorCodes {
    SQL_ERROR(11),
    BAD_REQUEST_USER_CREATE(66),
    BAD_ARGUMENTS_IN_SEARCH_PATH(67),

    ENTITY_NOT_FOUND(45),
    ENTITY_NOT_CREATED_OR_NOT_UPDATED(46),
    ENTITY_NOT_DELETED(47),
    SOME_EXCEPTION(21),
    RUNTIME_EXCEPTION(22),
    NULL_POINTER(23),
    ID_IS_NOT_LONG(33),
    NO_ENTITY_WITH_SUCH_ID(55),
    POOR_INFORMATION_TO_CREATE_UPDATE_ENTITY(77),
    CAN_NOT_CONVERT_REQUEST_TO_ENTITY(78),
    FATAL_ERROR(1);

    public int getCodeId() {
        return codeId;
    }

    private int codeId;

    ApplicationErrorCodes(int codeId) {
        this.codeId = codeId;
    }
}
