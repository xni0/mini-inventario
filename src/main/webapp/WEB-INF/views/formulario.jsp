<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Nuevo producto</title>
  <style>
    body{font-family: system-ui, sans-serif; max-width: 700px; margin: 2rem auto;}
    .row{margin-bottom: .8rem;}
    label{display:block; font-weight:600; margin-bottom:.25rem;}
    input[type=text], input[type=number]{width:100%; padding:.5rem;}
    .err{background:#ffecec; border:1px solid #ffb3b3; padding:.5rem; margin:.5rem 0;}
    .help{font-size:.9em; opacity:.7;}
  </style>
</head>
<body>
  <h1>Alta de producto</h1>

  <c:if test="${not empty errores}">
    <div class="err">
      <ul>
        <c:forEach var="e" items="${errores}">
          <li>${e}</li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/productos">
    <input type="hidden" name="action" value="save"/>

    <div class="row">
      <label for="nombre">Nombre *</label>
      <input id="nombre" name="nombre" type="text"
             value="${form.nombre}"
             required maxlength="100"/>
    </div>

    <div class="row">
      <label for="categoria">Categoría *</label>
      <input id="categoria" name="categoria" type="text"
             value="${form.categoria}"
             required maxlength="50"/>
      <div class="help">La categoría se guardará en minúsculas.</div>
    </div>

    <div class="row">
      <label for="precio">Precio (€) *</label>
      <input id="precio" name="precio" type="text" inputmode="decimal"
             value="${not empty form.precio ? form.precio : param.precio}"
             required/>
    </div>

    <div class="row">
      <label for="stockInicial">Stock inicial</label>
      <input id="stockInicial" name="stockInicial" type="number" min="0" step="1"
             value="<c:out value='${form.stock}' default='0'/>"/>
    </div>

    <div class="row">
      <button type="submit">Guardar</button>
      <a href="${pageContext.request.contextPath}/productos">Volver</a>
    </div>
  </form>
</body>
</html>
