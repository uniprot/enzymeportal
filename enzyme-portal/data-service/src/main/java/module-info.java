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

module uk.ac.ebi.ep.DataService {

    requires spring.boot.starter.data.jpa;
    requires spring.boot.starter.aop;
    requires spring.aop;
    requires org.aspectj.weaver;
    requires spring.boot.starter.jdbc;
    requires com.zaxxer.hikari;
    requires java.persistence;
    requires java.transaction;
    requires org.hibernate.orm.core;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.orm;
    requires spring.context;
    requires spring.expression;
    requires spring.tx;
    requires spring.beans;
    requires spring.aspects;
    requires lombok;
    requires spring.boot.starter;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.boot.starter.logging;
    requires org.apache.logging.slf4j;
    requires org.apache.logging.log4j;
    requires java.annotation;
    requires java.xml.bind;
    requires spring.core;
    requires spring.jcl;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires querydsl.apt;
    requires querydsl.jpa;
    requires querydsl.core;
    requires spring.boot.starter.cache;
    requires spring.context.support;
    requires ehcache;
    requires spring.boot.configuration.processor;
    requires java.validation;
    requires java.sql;

    exports uk.ac.ebi.ep.dataservice.service;
    exports uk.ac.ebi.ep.dataservice.dto;
    exports uk.ac.ebi.ep.dataservice.common;
    exports uk.ac.ebi.ep.dataservice.entities;

}
