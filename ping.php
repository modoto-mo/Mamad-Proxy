<?php
/**
 * Mamad Proxy - PHP TCP Socket Ping Backend
 * Used by scan.html for true server-side MTProto socket connectivity testing.
 */

header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

$server = isset($_GET['server']) ? trim($_GET['server']) : '';
$port = isset($_GET['port']) ? intval($_GET['port']) : 0;
$timeout = isset($_GET['timeout']) ? min(intval($_GET['timeout']), 3000) : 1000; // max 3s

if (empty($server) || $port <= 0 || $port > 65535) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid parameters. Specify server and port.'
    ]);
    exit();
}

$startTime = microtime(true);
$socket = @fsockopen($server, $port, $errno, $errstr, $timeout / 1000.0);

if ($socket) {
    $latencyMs = round((microtime(true) - $startTime) * 1000);
    fclose($socket);
    echo json_encode([
        'status' => 'ok',
        'server' => $server,
        'port' => $port,
        'ping' => $latencyMs
    ]);
} else {
    echo json_encode([
        'status' => 'offline',
        'server' => $server,
        'port' => $port,
        'ping' => -1,
        'error' => $errstr
    ]);
}
