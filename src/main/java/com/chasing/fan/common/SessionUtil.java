package com.chasing.fan.common;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

public class SessionUtil {
    public static Long getEmpId(HttpSession session) {
        return (Long) session.getAttribute("user");
    }

    public static void setEmpId(HttpSession session, Long empId) {
        session.setAttribute("user", empId);
    }

    public static void removeEmpId(HttpSession session) {
        session.removeAttribute("user");
    }

    public static Long getUserId(HttpSession session) {
        return (Long) session.getAttribute("user");
    }

    public static void setUserId(HttpSession session, Long userId) {
        session.setAttribute("user", userId);
    }
}
