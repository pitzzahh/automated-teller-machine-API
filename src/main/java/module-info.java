/**
 * Module info
 */
module automated.teller.machine.API {
    requires util.classes;
    requires spring.jdbc;
    requires java.sql;

    exports io.github.pitzzahh.atm.dao;
    exports io.github.pitzzahh.atm.database;
    exports io.github.pitzzahh.atm.entity;
    exports io.github.pitzzahh.atm.exceptions;
    exports io.github.pitzzahh.atm.mapper;
    exports io.github.pitzzahh.atm.service;
    exports io.github.pitzzahh.atm.validator;

}