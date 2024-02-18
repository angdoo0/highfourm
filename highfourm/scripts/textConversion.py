import pdfplumber
import json
import io
import sys
import re

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

pdf_path = sys.argv[1] 
# pdf_path = "c:/pdf/tt.pdf"

pdf_data = {
    "order": [],
    "detail": []
}

def clean_cell(cell):
    # 숫자인 경우 쉼표 제거 및 공백 제거
    if cell.replace(',', '').replace(' ', '').isdigit():
        return int(cell.replace(',', '').replace(' ', ''))
    return cell.strip()

with pdfplumber.open(pdf_path) as pdf:
    for page in pdf.pages:
        text = page.extract_text()
        if text:
            text = text.replace("담당자 ", "").replace("주문일 ", "").replace("납기일 ", "")
            order_lines = text.split("\n")
            order_lines = [line for i, line in enumerate(order_lines) if i in [0, 2, 3, len(order_lines) - 1] and line.strip()]
            if order_lines:  
                pdf_data["order"].append(order_lines)
        
        tables = page.extract_tables()
        for table in tables:
            table_rows = []
            for row in table:
                # 셀을 정제하고 빈 행이 아닌지 확인
                modified_row = [clean_cell(cell) for cell in row]
                if any(modified_row):  # 수정된 행에 내용이 있으면 추가
                    table_rows.append(modified_row)
            if table_rows:
                del table_rows[0]  # 헤더 제거
                pdf_data["detail"].append(table_rows)

json_data = json.dumps(pdf_data, ensure_ascii=False, indent=4)

print(json_data)
