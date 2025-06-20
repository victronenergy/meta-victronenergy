<?php
	$queryString = $_SERVER['QUERY_STRING'];
	$newLocation = '/gui-v2';

	// Check if there are any query parameters
	if (!empty($queryString)) {
		$newLocation .= '/?' . $queryString;
	}

	http_response_code(302);
	header("Location: $newLocation");
	exit();
?>
