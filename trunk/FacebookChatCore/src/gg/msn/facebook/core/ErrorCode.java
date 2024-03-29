package gg.msn.facebook.core;

/*
 * SIP Communicator, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
/**
 * Facebook Error Code
 * 
 * @author Dai Zhiwei
 * 
 */
public class ErrorCode {

    public static int kError_Global_ValidationError = 1346001;
    public static int kError_Login_GenericError = 1348009;
    public static int kError_Platform_CallbackValidationFailure = 1349007;
    public static int kError_Platform_ApplicationResponseInvalid = 1349008;
    public static int kError_Chat_NotAvailable = 1356002;
    public static int kError_Chat_SendOtherNotAvailable = 1356003;
    public static int kError_Chat_TooManyMessages = 1356008;
    public static int kError_Async_NotLoggedIn = 1357001;
    public static int kError_Async_LoginChanged = 1357003;
    public static int kError_Async_CSRFCheckFailed = 1357004;
    public static int Error_Global_NoError = 0;
    public static int Error_Async_HttpConnectionFailed = 1001;
    public static int Error_Async_UnexpectedNullResponse = 1002;
    public static int Error_System_UIDNotFound = 1003;
    public static int Error_System_ChannelNotFound = 1004;
    public static int Error_System_PostFormIDNotFound = 1005;
    public static int Error_Global_PostMethodError = 1006;
    public static int Error_Global_GetMethodError = 1007;
    public static int Error_Global_JSONError = 1008;
    public static int Error_System_UserNameNotFound = 1009;
    // 1357005: Error(1357005): Bad Parameter; There was an error understanding
    // the request.
}
