package com.hirix.exception;

public enum ApplicationErrorCodes {
    FATAL_ERROR(1),
    SQL_ERROR(11),
    SOME_EXCEPTION(21),
    RUNTIME_EXCEPTION(22),
    NULL_POINTER(23),
    ID_IS_NOT_REQUIRED_NUMBER_FORMAT(33),
    ENTITY_NOT_FOUND(45),
    ENTITY_NOT_CREATED_OR_NOT_UPDATED(46),
    ENTITY_NOT_DELETED(47),
    NO_ENTITY_WITH_SUCH_ID(55),
    BAD_REQUEST_SEE_DETAILS(66),
    BAD_ARGUMENTS_IN_SEARCH_PATH(67),
    POOR_INFORMATION_TO_CREATE_UPDATE_ENTITY(77),
    CAN_NOT_CONVERT_REQUEST_TO_ENTITY(78);

    public int getCodeId() {
        return codeId;
    }

    private int codeId;

    ApplicationErrorCodes(int codeId) {
        this.codeId = codeId;
    }
}
