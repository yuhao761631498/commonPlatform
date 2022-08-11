package com.gdu.model.user;

import java.io.Serializable;

public class LoginBean implements Serializable {

    /**
     * msg : 操作成功
     * code : 0
     * data : {"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOj
     * E2MDg2NDM0MzIsInVzZXJfbmFtZSI6ImZpcmVCdXR0b24iLCJhdXRob3JpdGllcyI6WyIyM
     * DAzIl0sImp0aSI6Ijk1Mzc5ZGQwLWY4NTItNGM5MS1hYTM0LTA2N2YxMDBmZDA5MiIsImNs
     * aWVudF9pZCI6ImZtcyIsInNjb3BlIjpbImFsbCJdfQ.W3UWwGEaK1jGKcG6CDJWB8uScWXY
     * PQkHAsRhIutgjZrigibK0g8IJZ9wmfb2k_9z289SnbZ3CtBKulhKtCWWxflkQdg5lBipjaT
     * 4wNhbOHdNEe4ee_Slp9CkQLUUap2bAAv0BbfA4fbhFxmCLjiOZSCDJiY9OP6WYo4TdSf8nz
     * fMfbPj3YGgqJWZrx9tSGkLzFHv0Qs1wBnxzSd-rU71-0dHmpyOKtYAkXxB-SX1x0vKzCXpC
     * Oh2cC1iQ397aAyTa_ExURwN6Uc7MepRn6ubS9b1SxaQMrLqpkeZICxcBaUciEhtYViAG663
     * uXahdYytacHk5vZayaPeZW2qSGCpyg",
     * "token_type":"bearer",
     * "refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVC
     * J9.eyJ1c2VyX25hbWUiOiJmaXJlQnV0dG9uIiwic2NvcGUiOlsiYWxsIl0sImF0aSI6Ijk1Mz
     * c5ZGQwLWY4NTItNGM5MS1hYTM0LTA2N2YxMDBmZDA5MiIsImV4cCI6MTYwODY0MzQzMiwiYXV
     * 0aG9yaXRpZXMiOlsiMjAwMyJdLCJqdGkiOiI5YmZmMjljOS00ZjRmLTRjMjAtOTFkZC1mYTA4
     * NTQ2Zjc5MWYiLCJjbGllbnRfaWQiOiJmbXMifQ.DImNMq8BUibHmkGq2o5Rd0Hm1XH4gXjMHC
     * vbsnalysNoxEqVovtX80hSQOA5xY9aO6p6clqGFBBPiDuCdjpIzGGz2jIR0KV1mtPlsNW4wQh
     * HwEFQu-bO5WnmbRFy2Zm-lKlujuQFKDgKSED6ABdjW6O6S8NH9W3jPfX3E9K3xzDP11y-bmpp
     * QvKG7mkV_cuUNJ3Q4k6uwnAQR6fbIpLdKxtnuAco9DEdDqAtUqtFwYoaKm3QcgJBG1mGA_og9
     * s_pdnkicA-sv-RKCsiDXwPw0tBg5Z_QLpzwPx2rW8RGqq_lP6SHesdaXZzSIvNkHM4Gx9u-bm
     * GLye2HrKrhgJ2QRQ","expires_in":14399,"scope":"all","jti":"95379dd0-f852-4
     * c91-aa34-067f100fd092"}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public  class DataBean implements Serializable{
        /**
         * access_token : eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDg2NDM0MzIsInVzZXJfbmFtZSI6ImZpcmVCdXR0b24iLCJhdXRob3JpdGllcyI6WyIyMDAzIl0sImp0aSI6Ijk1Mzc5ZGQwLWY4NTItNGM5MS1hYTM0LTA2N2YxMDBmZDA5MiIsImNsaWVudF9pZCI6ImZtcyIsInNjb3BlIjpbImFsbCJdfQ.W3UWwGEaK1jGKcG6CDJWB8uScWXYPQkHAsRhIutgjZrigibK0g8IJZ9wmfb2k_9z289SnbZ3CtBKulhKtCWWxflkQdg5lBipjaT4wNhbOHdNEe4ee_Slp9CkQLUUap2bAAv0BbfA4fbhFxmCLjiOZSCDJiY9OP6WYo4TdSf8nzfMfbPj3YGgqJWZrx9tSGkLzFHv0Qs1wBnxzSd-rU71-0dHmpyOKtYAkXxB-SX1x0vKzCXpCOh2cC1iQ397aAyTa_ExURwN6Uc7MepRn6ubS9b1SxaQMrLqpkeZICxcBaUciEhtYViAG663uXahdYytacHk5vZayaPeZW2qSGCpyg
         * token_type : bearer
         * refresh_token : eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJmaXJlQnV0dG9uIiwic2NvcGUiOlsiYWxsIl0sImF0aSI6Ijk1Mzc5ZGQwLWY4NTItNGM5MS1hYTM0LTA2N2YxMDBmZDA5MiIsImV4cCI6MTYwODY0MzQzMiwiYXV0aG9yaXRpZXMiOlsiMjAwMyJdLCJqdGkiOiI5YmZmMjljOS00ZjRmLTRjMjAtOTFkZC1mYTA4NTQ2Zjc5MWYiLCJjbGllbnRfaWQiOiJmbXMifQ.DImNMq8BUibHmkGq2o5Rd0Hm1XH4gXjMHCvbsnalysNoxEqVovtX80hSQOA5xY9aO6p6clqGFBBPiDuCdjpIzGGz2jIR0KV1mtPlsNW4wQhHwEFQu-bO5WnmbRFy2Zm-lKlujuQFKDgKSED6ABdjW6O6S8NH9W3jPfX3E9K3xzDP11y-bmppQvKG7mkV_cuUNJ3Q4k6uwnAQR6fbIpLdKxtnuAco9DEdDqAtUqtFwYoaKm3QcgJBG1mGA_og9s_pdnkicA-sv-RKCsiDXwPw0tBg5Z_QLpzwPx2rW8RGqq_lP6SHesdaXZzSIvNkHM4Gx9u-bmGLye2HrKrhgJ2QRQ
         * expires_in : 14399
         * scope : all
         * jti : 95379dd0-f852-4c91-aa34-067f100fd092
         */

        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private String jti;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getJti() {
            return jti;
        }

        public void setJti(String jti) {
            this.jti = jti;
        }
    }
}
