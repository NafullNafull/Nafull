덕담 받기

POST /api/v1/well-wishes/receive

덕담 보내기

POST /api/v1/well-wishes/send

덕담 잠금해제

PATCH /api/v1/well-wishes/:id/unlock

회원가입

POST /api/v1/users/register

덕담 URL로 조회 (= 단건조회)

GET /api/v1/well-wishes/:id

유저 조회

- 받은 덕담 목록
- 보낸 덕담 목록
- 전파한 덕담 개수
- 열쇠 개수

GET /api/v1/users/:id

서비스 가입자 디스코드 ID목록 조회

GET /api/v1/users/all/ids