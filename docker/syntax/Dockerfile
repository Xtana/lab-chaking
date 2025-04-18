# Используем официальный образ Python
FROM python:3.11-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем код из папки checker в контейнер
COPY ../../src/main/resources/checker /app/checker

# Запуск python с флагом для компиляции
#CMD ["python", "checker/solution.py"]
CMD ["python", "-m", "py_compile", "checker/solution.py"]
