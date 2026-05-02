package Server.Net;

import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

public interface CommandDispatcher {
    CommandResponse dispatch(CommandRequest request);
}