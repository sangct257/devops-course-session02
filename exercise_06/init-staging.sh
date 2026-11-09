#!/bin/bash

# Mã màu ANSI
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

CONTAINER_NAME="quickbite-db"
DB_PORT=5432
DB_USER="postgres"
DB_PASS="12345678"

echo -e "${YELLOW}=== GIAI ĐOẠN 1: DỌN DẸP TÀI NGUYÊN (CLEAN UP) ===${NC}"
# Tìm và xóa container cũ nếu tồn tại
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    echo "Phát hiện container '${CONTAINER_NAME}' cũ. Đang tiến hành dọn dẹp..."
    docker stop ${CONTAINER_NAME} >/dev/null 2>&1
    docker rm -f ${CONTAINER_NAME} >/dev/null 2>&1
    echo -e "${GREEN}[OK] Đã dọn dẹp container '${CONTAINER_NAME}' thành công.${NC}"
else
    echo "Không tìm thấy container '${CONTAINER_NAME}' cũ. Bỏ qua bước dọn dẹp."
fi

echo ""
echo -e "${YELLOW}=== GIAI ĐOẠN 2: KIỂM SOÁT CỔNG MẠNG (PORT CHECK - FAIL FAST) ===${NC}"
PORT_OCCUPIED=0

# Kiểm tra xem cổng 5432 có bị chiếm bởi tiến trình NGOÀI Docker hay không
# Sử dụng docker run thử gán port để kiểm tra chính xác tính sẵn sàng trên Host
if ! docker run --rm -p ${DB_PORT}:${DB_PORT} alpine echo "ok" >/dev/null 2>&1; then
    PORT_OCCUPIED=1
fi

if [ ${PORT_OCCUPIED} -eq 1 ]; then
    echo -e "${RED}[ERROR] Cổng ${DB_PORT} trên máy host đang bị chiếm dụng bởi tiến trình khác!${NC}"
    echo -e "${RED}Dừng script khởi tạo Staging.${NC}"
    exit 1
else
    echo -e "${GREEN}[OK] Cổng ${DB_PORT} đang trống. Sẵn sàng khởi tạo.${NC}"
fi

echo ""
echo -e "${YELLOW}=== GIAI ĐOẠN 3: KHỞI TẠO DATABASE ===${NC}"
echo "Đang khởi chạy container PostgreSQL '${CONTAINER_NAME}'..."
docker run -d \
    --name ${CONTAINER_NAME} \
    -p ${DB_PORT}:${DB_PORT} \
    -e POSTGRES_PASSWORD=${DB_PASS} \
    postgres:15-alpine >/dev/null

if [ $? -eq 0 ]; then
    echo -e "${GREEN}[OK] Container '${CONTAINER_NAME}' đã được khởi chạy ngầm thành công.${NC}"
else
    echo -e "${RED}[ERROR] Khởi chạy container thất bại.${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}=== GIAI ĐOẠN 4: KIỂM THỬ ĐỘ SẴN SÀNG (SMOKE TEST) ===${NC}"
echo "Tạm dừng 5 giây chờ Database sẵn sàng..."
sleep 5

docker exec ${CONTAINER_NAME} pg_isready -U ${DB_USER} >/dev/null 2>&1

if [ $? -eq 0 ]; then
    echo -e "${GREEN}====================================================${NC}"
    echo -e "${GREEN}  DATABASE STAGING KHỞI TẠO THÀNH CÔNG!            ${NC}"
    echo -e "${GREEN}====================================================${NC}"
    exit 0
else
    echo -e "${RED}====================================================${NC}"
    echo -e "${RED}  KHỞI TẠO DATABASE THẤT BẠI!                       ${NC}"
    echo -e "${RED}====================================================${NC}"
    echo -e "${YELLOW}Trích xuất 20 dòng log cuối cùng của container:${NC}"
    docker logs --tail 20 ${CONTAINER_NAME}
    exit 1
fi