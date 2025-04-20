FROM python:3.11-slim

WORKDIR /app

COPY ../../src/main/resources/checker/solution.py .
COPY ../../src/main/resources/checker/test.py .

CMD ["python", "test.py"]