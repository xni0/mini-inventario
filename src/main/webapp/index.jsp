<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MiniInventario</title>
    <style>
        body {
            font-family: system-ui, sans-serif;
            max-width: 700px;
            margin: 3rem auto;
            padding: 1rem;
        }
        h1 {
            margin-bottom: 1.5rem;
        }
        a {
            font-size: 1.1rem;
            color: #0366d6;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        .box {
            padding: 1.5rem;
            border: 1px solid #ddd;
            border-radius: 8px;
            background: #fafafa;
        }
    </style>
</head>
<body>

    <div class="box">
        <h1>MiniInventario</h1>
        <p>Bienvenido. Accede a la gestión de productos:</p>
        <p><a href="${pageContext.request.contextPath}/productos">Ir a Productos</a></p>
    </div>

</body>
</html>
