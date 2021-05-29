FROM httpd:alpine
COPY ./dist/MapareVote /usr/local/apache2/htdocs/

#copy htaccess and httpd.conf to container
COPY .htaccess /usr/local/apache2/htdocs/
COPY httpd.conf /usr/local/apache2/conf/httpd.conf

#change permissions
RUN chmod -R 755 /usr/local/apache2/htdocs/

#expose port
EXPOSE 5433