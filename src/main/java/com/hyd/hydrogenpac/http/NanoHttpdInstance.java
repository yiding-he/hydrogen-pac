package com.hyd.hydrogenpac.http;

import com.hyd.hydrogenpac.pac.PacTemplate;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NanoHttpdInstance extends NanoHTTPD {

    public NanoHttpdInstance(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        // 不论访问什么路径都只返回 PAC 内容
        try {
            String pacContent = PacTemplate.generatePac();
            return newFixedLengthResponse(
                Status.OK, "text/javascript", pacContent
            );
        } catch (IOException e) {
            log.error("", e);
            return newFixedLengthResponse(Status.INTERNAL_ERROR, "text/plain", "");
        }
    }
}
