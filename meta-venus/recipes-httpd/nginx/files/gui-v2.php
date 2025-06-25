<?php
	http_response_code(302);
	header("Location: /gui-v2" . (empty($_SERVER['QUERY_STRING']) ? "" : "/?" . $_SERVER['QUERY_STRING']));
	exit();
?>

