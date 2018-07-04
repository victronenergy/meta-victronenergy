<?php
	// Logo location
	define('LOGO_FILE', '/data/themes/overlay/mobile-builder-logo.png');

	// Handle retrieving logo
	if (isset($_GET['get'])) {
		header('Content-Type: image/png');
		readfile(LOGO_FILE);
		exit();
	}

	// Handle setting logo
	if (isset($_GET['set'])) {
		if (isset($_SERVER['HTTP_CONTENT_ENCODING']) && $_SERVER['HTTP_CONTENT_ENCODING'] === 'base64') {
			$data = base64_decode(file_get_contents('php://input'));
		} else {
			$data = fopen('php://input', 'r');
		}
		if (file_put_contents(LOGO_FILE, $data)) {
			echo 'ok';
		}
		exit();
	}

	// Display web interface
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
	<title>Victron Energy - Logo Changer</title>
	<link rel="icon" href="data:image/x-icon;base64,AAABAAEAEBAAAAEAIABoBAAAFgAAACgAAAAQAAAAIAAAAAEAIAAAAAAAQAQAABMLAAATCwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANCQRxHQkEex0JBH7tCQR7PQkEed0JBHrtCQR7PQkEee0JBHx9CQR28AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADQkEed0JBHodCQR77QkEfZ0JBHptCQR8rQkEeg0JBH4dCQR3jQkEfp0JBHPAAAAAAAAAAAAAAAAAAAAADQkEcC0JBH7dCQRyfQkEfw0JBHHtCQR/HQkEcz0JBH59CQR13QkEe50JBHedCQR5oAAAAAAAAAAAAAAAAAAAAA0JBHIdCQR9vQkEdB0JBHttCQR1DQkEfH0JBHdtCQR+TQkEc30JBH79CQRzPQkEfU0JBHatCQRwLQkEdk0JBHEdCQR2DQkEek0JBHftCQR5jQkEeD0JBHrtCQR5LQkEfD0JBHNdCQR/nQkEcb0JBH9dCQR/XQkEcb0JBH+NCQRzXQkEfD0JBHkdCQR63QkEeD0JBHmNCQR33QkEem0JBHX9CQRxHQkEdj0JBHAtCQR2rQkEfU0JBHNNCQR+fQkEc40JBH5NCQR3LQkEfH0JBHUNCQR7bQkEdC0JBH29CQRyIAAAAAAAAAAAAAAAAAAAAA0JBHm9CQR3nQkEe00JBHX9CQR+fQkEcz0JBH8dCQRx3QkEfw0JBHJ9CQR+3QkEcCAAAAAAAAAAAAAAAAAAAAANCQRzzQkEfp0JBHeNCQR+PQkEeg0JBHytCQR6bQkEfa0JBHwNCQR6LQkEedAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA0JBHbtCQR8XQkEeb0JBHtNCQR6zQkEeg0JBHs9CQR+7QkEex0JBHEQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8bG///ZW//////+AH///gAAADwAPC/8AAAAAAAAAAAAHJ+AA9kZQAPAAAAHwAAgB8AAP//AAD//7+///+Ojg==" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<style type="text/css">
	body {
		background: #eee;
		font-family: sans-serif;
		margin-top: 5%;
	}
	h1 {
		display: block;
		margin: 2rem auto;
		width: 40rem;
		text-align: center;
		line-height: 3rem;
		text-indent: 0;
		font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
		font-weight: bold;
		font-size: 2rem;
		color: #222;
	}
	#container {
		width: 40rem;
		margin: 0 auto;
		background: #fff;
		border: 1px solid #ddd;
		border-radius: 5px;
		padding: 15px;
		line-height: 4rem;
		text-align: center;
	}
	#container .notice {
		line-height: 1rem;
		font-size: 0.8rem;
		font-style: italic;
	}
	@keyframes loader-delay {
		0%, 80%, 100% {
			transform: scale(0);
		} 40% {
			transform: scale(1.0);
		}
	}
	@media (max-width: 44rem) {
		h1, #container {
			width: calc(100% - 4rem);
		}
	}
	</style>
</head>
<body>
	<h1>Logo Changer</h1>
	<div id="container">
		<div id="msg"></div>
		<div class="uploader">
			<canvas width="208" height="118" crossorigin="anonymous"></canvas><br />
			<input type="file" /><br />
			<div class="notice">Note: Changes will only be applied after rebooting the device.</span>
		</div>
	</div>
	<script type="text/javascript">
		(() => {
			// Elements
			const container = document.getElementById('container');
			const uploader = container.querySelector('.uploader');
			const canvas = uploader.querySelector('canvas');
			const context = canvas.getContext('2d');
			const input = uploader.querySelector('input');
			const notice = uploader.querySelector('.notice');
			msg = document.getElementById('msg');

			// Function for getting latest image
			function get() {
				fetch('?get')
					.then((response) => {
						if(response.ok) {
							return response.blob();
						} else {
							msg.innerHTML = 'Failed to retrieve current logo: ' + response.statusText;
						}
					}, (reason) => {
						msg.innerHTML = 'Failed to connect: ' + reason;
					})
					.then((blob) => {
						const image = new Image();
						image.addEventListener('load', () => {
							// Draw logo
							context.clearRect(0, 0, 208, 118);
							context.drawImage(image, 0, 0, 208, 118);
						});
						image.addEventListener('error', () => {
							msg.innerHTML = 'No custom logo set.';
						});
						image.src = URL.createObjectURL(blob);
					}, (reason) => {
						msg.innerHTML = 'Failed to load logo: ' + reason;
					});
			}

			function set(data) {
				// Post new image
				let headers = {
					'Content-Type': 'image/png'
				};
				if (typeof data === 'string') {
					headers['Content-Encoding'] = 'base64';
				}
				fetch('?set', {
					method: 'post',
					headers: headers,
					body: data
				}).then((response) => {
					if(response.ok) {
						return response.text();
					} else {
						msg.innerHTML = 'Failed to set new logo: ' + response.statusText;
					}
				}, (reason) => {
					msg.innerHTML = 'Failed to submit new logo: ' + reason;
				}).then((response) => {
					if(response == 'ok') {
						notice.innerHTML = 'Note: Reboot the device to apply the changed logo.';
						notice.style.fontWeight = 'bold';
						get();
					} else {
						msg.innerHTML = 'Error while setting new logo: ' + response;
					}
				}, (reason) => {
					msg.innerHTML = 'Invalid response while setting logo: ' + reason;
				});
			}

			// Function for uploading new image
			input.addEventListener('change', (event) => {
				const file = event.target.files[0];
				event.target.value = '';
				msg.innerHTML = ""

				// Check type
				if(!file.type.match (/^image\//)) {
					window.alert('Select a valid image file.');
					return;
				}

				// Read image
				const reader = new FileReader();
				reader.addEventListener('load', () => {
					const newImage = new Image();
					newImage.addEventListener('load', () => {
						// Convert image
						const newCanvas = document.createElement('canvas');
						newCanvas.setAttribute('width', 208);
						newCanvas.setAttribute('height', 118);
						const newContext = newCanvas.getContext('2d');
						newContext.drawImage(newImage, 0, 0, 208, 118);

						// Support older browsers
						if('toBlob' in newCanvas) {
							newCanvas.toBlob(set);
						} else {
							set(newCanvas.toDataURL().replace(/^data\:image\/png\;base64\,/, ''));
						}
					});
					newImage.addEventListener('error', () => {
						window.alert('Unsupported image type.');
					});
					newImage.src = reader.result;
				});
				reader.readAsDataURL(file);
			});

			// Retrieve logo on pageload
			get();
		})();
	</script>
</body>
</html>
