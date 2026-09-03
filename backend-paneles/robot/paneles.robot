*** Settings ***
Library    RequestsLibrary
Library    Collections
Suite Setup    Crear sesion API
Suite Teardown    Delete All Sessions

*** Variables ***
${BASE_URL}    http://localhost:8082
${USER_ID}    robot-user
${PANEL_ID}    ${EMPTY}

*** Test Cases ***
Crear panel con usuario de pruebas
    ${payload}=    Create Dictionary
    ...    nombre=Panel Robot
    ...    color=#3498db
    ...    prioridad=${1}
    ...    fechaInicio=2026-09-01
    ...    fechaFin=2026-09-30
    ${response}=    POST On Session    paneles    /api/paneles    json=${payload}    headers=${AUTH_HEADERS}
    Should Be Equal As Integers    ${response.status_code}    201
    Should Be Equal As Strings    ${response.json()['nombre']}    Panel Robot
    Should Be Equal As Strings    ${response.json()['propietarioId']}    ${USER_ID}
    Should Be Equal As Strings    ${response.json()['estado']}    PENDIENTE
    Set Suite Variable    ${PANEL_ID}    ${response.json()['id']}

Listar paneles del usuario de pruebas
    ${response}=    GET On Session    paneles    /api/paneles    headers=${AUTH_HEADERS}
    Should Be Equal As Integers    ${response.status_code}    200
    ${paneles}=    Set Variable    ${response.json()}
    Should Not Be Empty    ${paneles}
    Should Be Equal As Strings    ${paneles[0]['propietarioId']}    ${USER_ID}

Actualizar estado del panel
    ${response}=    PUT On Session    paneles    /api/paneles/${PANEL_ID}/estado    data="EN_PROGRESO"    headers=${JSON_AUTH_HEADERS}
    Should Be Equal As Integers    ${response.status_code}    200
    Should Be Equal As Strings    ${response.json()['estado']}    EN_PROGRESO

Rechazar solicitud sin usuario
    ${response}=    GET On Session    paneles    /api/paneles    expected_status=401
    Should Be Equal As Integers    ${response.status_code}    401

Rechazar panel con nombre vacio
    ${payload}=    Create Dictionary    nombre=${EMPTY}
    ${response}=    POST On Session    paneles    /api/paneles    json=${payload}    headers=${AUTH_HEADERS}    expected_status=400
    Should Be Equal As Integers    ${response.status_code}    400

*** Keywords ***
Crear sesion API
    Create Session    paneles    ${BASE_URL}
    ${AUTH_HEADERS}=    Create Dictionary    X-User-Id=${USER_ID}
    ${JSON_AUTH_HEADERS}=    Create Dictionary    X-User-Id=${USER_ID}    Content-Type=application/json
    Set Suite Variable    ${AUTH_HEADERS}
    Set Suite Variable    ${JSON_AUTH_HEADERS}