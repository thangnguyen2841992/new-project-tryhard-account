package com.regain.accountservicemaven.security;

public class EndPoints {
    public static final String FRONT_END_HOST = "http://localhost:3000";
    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/watches/**",
            "/images/**",
            "/brands/**",
            "/categories/**",
            "/users/search/**",
            "/user-api/active/**",
            "/user-api/checkMatchPassword/**",
            "/carts/**"
    };
    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/account/createNewAccount",
            "/account/loginAccount",
            "/account/activeAccount",
            "/account/**"
    };
    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/users",
            "/users/**",
            "/carts/**"
    };
    public static final String[] ADMIN_DELETE_ENDPOINTS = {
            "/users/**",
            "/watch-api/**",
    };
    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/watch-api/**",
    };

    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/watches/**",
            "/watches",
            "/watch-api/**",
            "/watch-api",
            "/carts/**"
    };
    public static final String[] USER_GET_ENDPOINTS = {
            "/users",
            "/users/**",
            "/cart-api",
            "/cart-api/**",
            "/carts/**",
            "/order-api",
            "/order-api/**",
    };
    public static final String[] USER_POST_ENDPOINTS = {
            "/watches/**",
            "/watches",
            "/watch-api/**",
            "/watch-api",
            "/cart-api",
            "/cart-api/**",
            "/carts/**",
            "/order-api",
            "/order-api/**",
    };
    public static final String[] USER_DELETE_ENDPOINTS = {
            "/cart-api/**",
    };
}
