const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const HOST = '0.0.0.0';
const HTML_FILE = path.join(__dirname, 'export_web', 'index.html');

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon',
  '.txt': 'text/plain; charset=utf-8'
};

process.on('uncaughtException', (err) => {
  console.error('Uncaught Exception:', err);
});
process.on('unhandledRejection', (reason, promise) => {
  console.error('Unhandled Rejection at:', promise, 'reason:', reason);
});

const server = http.createServer((req, res) => {
  req.on('error', (err) => {
    console.error('Req error:', err);
  });
  res.on('error', (err) => {
    console.error('Res error:', err);
  });

  // Normalize URL
  const parsedUrl = new URL(req.url, `http://${req.headers.host || 'localhost'}`);
  let pathname = parsedUrl.pathname;

  // Security check against directory traversal
  if (pathname.includes('..')) {
    res.writeHead(403, { 'Content-Type': 'text/plain' });
    return res.end('Forbidden');
  }

  // Handle health check
  if (pathname === '/healthz' || pathname === '/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    return res.end(JSON.stringify({ status: 'ok', app: 'Nesmat Alhayat Web' }));
  }

  // Default to export_web/index.html for SPA routes
  let filePath = HTML_FILE;
  if (pathname !== '/' && pathname !== '/index.html') {
    const candidatePath = path.join(__dirname, pathname);
    if (fs.existsSync(candidatePath) && fs.statSync(candidatePath).isFile()) {
      filePath = candidatePath;
    }
  }

  fs.readFile(filePath, (err, data) => {
    if (err) {
      // Fallback to index.html for SPA routing
      fs.readFile(HTML_FILE, (err2, fallbackData) => {
        if (err2) {
          res.writeHead(500, { 'Content-Type': 'text/plain; charset=utf-8' });
          return res.end('خطأ في تحميل الصفحة');
        }
        res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
        res.end(fallbackData);
      });
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';
    res.writeHead(200, {
      'Content-Type': contentType,
      'Cache-Control': 'no-cache',
      'Access-Control-Allow-Origin': '*'
    });
    res.end(data);
  });
});

server.listen(PORT, HOST, () => {
  console.log(`🌿 نسمة الحياة Web Server listening on http://${HOST}:${PORT}`);
});
