package com.tugalsan.api.os.server;

import module com.tugalsan.api.union;
import module com.tugalsan.api.function;

public class TS_OsBrowserUtils {

    public static TGS_UnionExcuseVoid launch(String url) {
        return TGS_FuncMTCUtils.call(() -> {
            if (TS_OsPlatformUtils.isMac()) {
                var macUtils = Class.forName("com.apple.mrj.MRJFileUtils");
                var openURL = macUtils.getDeclaredMethod("openURL", String.class);
                openURL.invoke(null, url);
            } else if (TS_OsPlatformUtils.isWindows()) {
                Runtime.getRuntime().exec(new String[]{"rundll32 url.dll,FileProtocolHandler ", url});
            } else { //assume Unix or Linux
                String[] browsers = {
                    "firefox", "opera", "konqueror", "mozilla", "netscape"};
                String browser = null;
                for (var count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("could.not.find.web.browser");
                } else {
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> TGS_UnionExcuseVoid.ofExcuse(e));
    }
}
