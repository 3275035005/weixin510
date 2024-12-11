package com.sky.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: WebSocketServer
 * USER: SHINIAN
 * DATE: 2023/5/26
 * DESCRIPTION : WebSocket的服务器端
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{clientId}") //用于声明当客户端请求的路径为/ws并且携带路径参数的时候,自动映射到此类
public class WebSocketServer {

    //保存表示客户端的Session对象,在客户端请求连接的时候就获取到客户端的Id与对应的Session对象保存到Map中
    private static final Map<String, Session> SESSION_MAP = new HashMap<>();

    /**
     * 服务器端给当前SESSION_MAP中保存的客户端依次发送消息
     *
     * @param message 要发送给客户端的消息信息
     */
    public void serverSendMessage2AllClient(String message) throws IOException {
        Collection<Session> sessionCollection = SESSION_MAP.values();
        for (Session session : sessionCollection) {
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 当客户端基于ws://localhost:8080/ws/{随机值}发出请求的时候发送数据到服务器端,服务器端接收客户端发送的字符串数据映射到方法的参数
     *
     * @param message  客户端携带的参数信息
     * @param clientId 客户端携带的Id值(用于保存到Map中作为唯一Key与Session对象映射)
     */
    @OnMessage
    public void clientSendMessage2Server(String message, @PathParam("clientId") String clientId) {
        log.info("WebSocket服务器接收到客户端 : {}的发送消息,消息内容是 : {}", clientId, message);
    }

    /**
     * 当客户端基于ws://localhost:8080/ws/{随机值}发出请求的时候表示请求建立链接,服务器端接收到请求之后,将表示客户端的Session对象与携带的随机值保存到Map中
     *
     * @param session  表示本次基于WebSocket协议发出连接请求的客户端对象(里面包含了客户端的基本信息)
     * @param clientId 客户端携带的Id值(用于保存到Map中作为唯一Key与Session对象映射)
     */
    @OnOpen
    public void clientConnection2Server(Session session, @PathParam("clientId") String clientId) {
        log.info("WebSocket服务器接收到客户端 : {}的链接请求,已建立链接并保存客户端信息", clientId);
        SESSION_MAP.put(clientId, session);
    }

    /**
     * 当客户端基于ws://localhost:8080/ws/{随机值}发出请求的时候请求断开链接,服务器端接收到请求之后,在Map中将对应的KEY的Session客户端从Map中删除
     *
     * @param clientId 客户端携带的Id值(用于保存到Map中作为唯一Key与Session对象映射)
     */
    @OnClose
    public void clientDisconnection2Server(@PathParam("clientId") String clientId) {
        log.info("WebSocket服务器接收到客户端 : {}的断开请求,已断开连接并移除客户端信息", clientId);
        SESSION_MAP.remove(clientId);
    }
}
