package com.bai.service.Imp;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bai.pojo.vo.ChatVO;
import com.bai.service.ChatService;
import com.bai.service.ConsultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Author:XY
 * PACkAGE:com.bai.service.Imp
 * Date:2023/6/5 19:37
 */
@Service
@Slf4j
public class ConsultServiceImpl extends TextWebSocketHandler implements ConsultService {
    private static final String template2 = "<li class=\"online chat-title\" data-id=\"%s\"><div class=\"hover_action\"><button class=\"btn btn-link text-info\" data-original-title=\"标记为公开\" data-toggle=\"tooltip\" onclick=\"openChat('%s')\" type=\"button\"><i class=\"zmdi zmdi-eye\"></i></button><button class=\"btn btn-link text-warning\" data-answer=\"点击我来跟chatgpt聊天吧\" data-original-title=\"修改聊天\" data-title=\"新建聊天\" onclick=\"editChat('%s')\" type=\"button\"><i class=\"zmdi zmdi-edit\"></i></button><button class=\"btn btn-link text-danger\" data-original-title=\"移除聊天\" data-toggle=\"tooltip\" onclick=\"removeChat('%s')\" type=\"button\"><i class=\"zmdi zmdi-delete\"></i></button></div><a class=\"card\" href=\"#\" onclick=\"changeSessionId('%s','%s','%s')\"><div class=\"card-body\"><div class=\"media\"><div class=\"avatar me-3\"><div class=\"avatar rounded-circle no-image bg-primary text-light\"><span class=\"msg-avatar\"><img src=\"http://hoppinzq.com/zui/static/picture/0.jpg\" class=\"avatar avatar-lg rounded-circle\" style=\"filter: none\"></span></div></div><div class=\"media-body overflow-hidden\"><div class=\"d-flex align-items-center mb-1\"><h6 class=\"text-truncate mb-0 me-auto chat-question-header\">%s</h6><p class=\"small text-muted text-nowrap ms-4 mb-0\">%s</p></div><div class=\"text-truncate chat-answer-header\"><font color=\"red\">当前在线</font></div></div></div></div></a></li>";
    public static volatile WebSocketSession admin;
    @Autowired
    private ChatService chatService;

    private void extracted(WebSocketSession session, boolean isAdd) {
        Map<String, Object> attributes = session.getAttributes();
        Object uid = attributes.get("uid");
        Object id = attributes.get("id");
        if (id != null && uid != null) {
            if (!isAdd) {
                sessionsMap.remove(uid.toString());
            } else {
                if (Objects.equals(id, 0L)) {
                    if (admin != null) {
                        Map<String, Object> attributes1 = admin.getAttributes();
                        session.getAttributes().putAll(attributes1);
                    }
                    admin = session;
                    System.out.println("成功建立连接，管理员id：" + uid);
                } else {
                    System.out.println("成功建立连接，读者id：" + uid);
                    sessionsMap.put(uid.toString(), session);
                }
            }
            updateOnlineUser();
        }

    }

    private void updateOnlineUser() {
        if (admin != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : sessionsMap.keySet()) {
                WebSocketSession webSocketSession = sessionsMap.get(s);
                Map<String, Object> attributes = webSocketSession.getAttributes();
                Object logging_time = attributes.get("logging_time");
                String uid = attributes.get("uid").toString();
                Object uname = attributes.get("uname");
                stringBuilder.append(String.format(template2, uid, uid, uid, uid, uid, uid, uname, uname, logging_time));
            }
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("status", 2);
            stringObjectHashMap.put("body", stringBuilder.toString());
            aVoid(admin, new TextMessage(JSONUtil.toJsonStr(stringObjectHashMap)));

        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

      /*  String ip = session.getHandshakeHeaders().getFirst("X-Forwarded-For");
        if (ip == null) {
            ip = Objects.requireNonNull(session.getRemoteAddress()).getHostName();
        }
        log.info("成功建立连接：" + ip);
        System.out.println("成功建立连接：" + ip);
        if (Objects.equals(0L, session.getAttributes().get("id"))) {
            sessionsMap.put("admin", session);
        } else *//*sessionsMap.put(session.getId(), session);*//*sessionsMap.put(ip, session);*/

        try {
            super.afterConnectionEstablished(session);
            extracted(session, true);
        } catch (Exception e) {

        }

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        try {
            super.handleMessage(session, message);
        } catch (Exception e) {

        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            super.handleTextMessage(session, message);
            if (sendHeartBeat(message)) return;
            forwardMsg(message);
        } catch (Exception e) {

        }
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        try {
            super.handlePongMessage(session, message);
        } catch (Exception e) {

        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        try {
            super.handleTransportError(session, exception);
        } catch (Exception e) {

        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // super.afterConnectionClosed(session, status);
        // String ip = session.getHandshakeHeaders().getFirst("X-Forwarded-For");
        // if (ip == null) {
        //     ip = Objects.requireNonNull(session.getRemoteAddress()).getHostName();
        // }
        // sessionsMap.remove(ip);
        // Map<String, Object> attributes = session.getAttributes();
        // Object uid = attributes.get("uid");
        // Object id = attributes.get("id");
        // sessionsMap.remove(uid.toString());
        // if (Objects.equals(id, 0L)) admin = null;
        try {
            extracted(session, false);
        } catch (Exception e) {
            log.debug("管理员关闭聊天窗口： {}", JSONUtil.toJsonStr(session.getAttributes()));
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }

    /**
     * 转发消息逻辑
     *
     * @param message 消息
     * @throws IOException ioexception
     */
    @Override
    public void forwardMsg(TextMessage message) throws IOException {
        try {
            ChatVO chatVO = JSONUtil.toBean(message.getPayload(), ChatVO.class);
            for (String s : sessionsMap.keySet()) {
                WebSocketSession webSocketSession = sessionsMap.get(s);
                if (chatVO.getMessageId() == null) {
                    if (admin == null) {
                        admin_logout(-2, "-----当前管理员不在线----");
                        log.warn("-----当前管理员不在线----");
                    } else {
                        chatVO.setMessageId(admin.getId());
                        chatVO.setReceiverName(admin.getAttributes().get("uname").toString());
                        chatVO.setReceiverId(Long.parseLong(admin.getAttributes().get("uid").toString()));
                        String onlineKey = admin.getAttributes().get("onlineKey") + "";
                        if (StrUtil.isNotBlank(chatVO.getOnlineFlag()) && StrUtil.isNotBlank(onlineKey) && chatVO.getOnlineFlag().equals(onlineKey)) {
                            admin.sendMessage(new TextMessage(JSONUtil.toJsonStr(chatVO)));
                        } else {
                            // todo 收集其他人向管理员发来的消息，并在admin的页面上展示其他人发来的消息
                            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
                            stringObjectHashMap.put("status", 3);
                            stringObjectHashMap.put("body", chatVO.getSenderName());
                            TextMessage textMessage = new TextMessage(JSONUtil.toJsonStr(stringObjectHashMap));
                            admin.sendMessage(textMessage);
                        }
                        chatService.saveChat(chatVO);
                    }

                    return;
                }
          /*  if (webSocketSession.getId().equals(chatVO.getMessageId())) {
                chatService.saveChat(chatVO);
                webSocketSession.sendMessage(new TextMessage(JSONUtil.toJsonStr(chatVO)));
            }*/
                // 改用ip作为key
                if (s.equals(chatVO.getMessageId())) {
                    chatService.saveChat(chatVO);
                    webSocketSession.sendMessage(new TextMessage(JSONUtil.toJsonStr(chatVO)));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void aVoid(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.debug("消息发送失败aVoid：{}", this.getClass().toString());
        }
    }

    public boolean sendHeartBeat(TextMessage message) {

        try {
            if ("heartbeat".equals(message.getPayload())) {
                HashMap<String, Object> stringObjectHashMap = new HashMap<>();
                stringObjectHashMap.put("status", 1);
                stringObjectHashMap.put("body", "heartbeat");
                TextMessage textMessage = new TextMessage(JSONUtil.toJsonStr(stringObjectHashMap));
                if (admin != null && admin.isOpen()) admin.sendMessage(textMessage);
                sessionsMap.values().forEach(session -> {
                    if (session.isOpen()) aVoid(session, textMessage);
                });
                return true;
            }
        } catch (IOException e) {
            log.debug("心跳检测失败：{}", this.getClass().toString());
        }

        return false;
    }

    public boolean admin_logout(int status, String msg) {
        try {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("status", status);
            stringObjectHashMap.put("body", msg);
            TextMessage textMessage = new TextMessage(JSONUtil.toJsonStr(stringObjectHashMap));
            sessionsMap.values().forEach(session -> {
                aVoid(session, textMessage);
            });
            if (status == -1) admin = null;
            return true;
        } catch (RuntimeException e) {
            log.debug("心跳检测失败：{}", this.getClass().toString());
        }
        return false;
    }
}
