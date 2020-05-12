/*
 * The MIT License
 *
 * Copyright 2020 joseph.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

module EnzymeService {
    requires EpRestclient;
    requires okhttp3;
    requires okio;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.commons.codec;
    requires org.apache.httpcomponents.httpasyncclient;
    requires org.apache.httpcomponents.httpcore.nio;
    requires spring.boot.starter.webflux;
    requires spring.boot.starter;
    requires spring.boot.starter.logging;
    requires org.apache.logging.slf4j;
    requires org.apache.logging.log4j;
    requires jul.to.slf4j;
    requires java.annotation;
    requires spring.boot.starter.json;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.paramnames;
    requires spring.boot.starter.reactor.netty;
    requires reactor.netty;
    requires io.netty.codec.http;
    requires io.netty.common;
    requires io.netty.buffer;
    requires io.netty.transport;
    requires io.netty.resolver;
    requires io.netty.codec;
    requires io.netty.codec.http2;
    requires io.netty.handler;
    requires io.netty.handler.proxy;
    requires io.netty.codec.socks;
    requires io.netty.transport.epoll;
    requires io.netty.transport.unix.common;
    requires jakarta.el;
    requires spring.boot.starter.validation;
    requires java.validation;
    requires org.hibernate.validator;
    requires org.jboss.logging;
    requires com.fasterxml.classmate;
    requires spring.web;
    requires spring.beans;
    requires spring.webflux;
    requires nio.multipart.parser;
    requires nio.stream.storage;
    requires spring.boot;
    requires spring.context;
    requires spring.aop;
    requires spring.expression;
    requires spring.boot.autoconfigure;
    requires lombok;
    requires logback.classic;
    requires logback.core;
    requires org.slf4j;
    requires logback.json.classic;
    requires logback.json.core;
    requires logback.jackson;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires reactor.core;
    requires org.reactivestreams;
    requires java.xml.bind;
    requires spring.core;
    requires spring.jcl;
    requires org.apache.commons.io;
    //requires java.activation;
    //requires activation;
    requires com.sun.xml.bind;
    requires com.sun.xml.txw2;
    requires com.sun.istack.runtime;
    requires org.jvnet.staxex;
    requires com.sun.xml.fastinfoset;
    requires jakarta.activation;

    requires spring.boot.configuration.processor;
    exports uk.ac.ebi.ep.enzymeservice.reactome.view;
    exports uk.ac.ebi.ep.enzymeservice.reactome.model;
    exports uk.ac.ebi.ep.enzymeservice.reactome.service;
    exports uk.ac.ebi.ep.enzymeservice.uniprot.model;
    exports uk.ac.ebi.ep.enzymeservice.uniprot.service;
    exports uk.ac.ebi.ep.enzymeservice.intenz.dto;
    exports uk.ac.ebi.ep.enzymeservice.intenz.service;

}
